package org.wcong.test.spring.jpa;

import org.springframework.data.repository.CrudRepository;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/20
 */
public interface DbStockRepository extends CrudRepository<DbStock, Integer> {
}
