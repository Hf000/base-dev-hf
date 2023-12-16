package org.hf.boot.springboot.enumerate.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * spring data jpa数据层接口
 */
public interface JpaNewsEntityRepository extends JpaRepository<JpaNewsEntity, Integer> {
}