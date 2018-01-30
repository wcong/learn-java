package org.wcong.test.rxjava;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class MyPublisher {

    private static CountDownLatch countDownLatch = new CountDownLatch(4);

    public static void main(String[] args) throws InterruptedException {
        new ParallelFlowableRange(1, 100, Schedulers.computation()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println(Thread.currentThread().getName() + ":" + integer);
            }
        });
        countDownLatch.await();
    }


    public static class ParallelFlowableRange extends Flowable<Integer> {

        private Scheduler scheduler;

        private int start;
        private int end;

        public ParallelFlowableRange(int start, int end, Scheduler scheduler) {
            this.scheduler = scheduler;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void subscribeActual(Subscriber<? super Integer> subscriber) {
            AtomicInteger atomicInteger = new AtomicInteger(start);
            for (int i = 0; i < 4; i++) {
                MySubscriber mySubscriber = new MySubscriber(subscriber, Integer.MAX_VALUE, scheduler.createWorker(), atomicInteger, end);
                mySubscriber.onSubscribe(mySubscriber);
            }
        }
    }

    public static class MySubscriber extends AtomicInteger implements FlowableSubscriber<Integer>, Subscription, Runnable {

        private int prefetch;
        private Scheduler.Worker worker;
        private Subscriber<? super Integer> actual;
        private boolean cancel;
        private AtomicInteger atomicInteger;
        private int end;

        public MySubscriber(Subscriber<? super Integer> actual, int prefetch, Scheduler.Worker worker, AtomicInteger atomicInteger, int end) {
            this.actual = actual;
            this.prefetch = prefetch;
            this.worker = worker;
            this.atomicInteger = atomicInteger;
            this.end = end;
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            subscription.request(prefetch);
        }

        @Override
        public void onNext(Integer integer) {
            actual.onNext(integer);
        }

        @Override
        public void onError(Throwable throwable) {
            actual.onError(throwable);
        }

        @Override
        public void onComplete() {
            countDownLatch.countDown();
            actual.onComplete();
        }

        @Override
        public void request(long l) {
            worker.schedule(this);
        }

        @Override
        public void cancel() {
            cancel = true;
        }

        @Override
        public void run() {
            while (true) {
                if (cancel) {
                    break;
                }
                int num = atomicInteger.getAndIncrement();
                if (num < end) {
                    try {
                        onNext(num);
                    } catch (Exception e) {
                        onError(e);
                    }
                } else {
                    onComplete();
                    break;
                }
            }
        }
    }
}
