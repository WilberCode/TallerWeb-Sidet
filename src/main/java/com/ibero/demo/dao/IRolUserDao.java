package com.ibero.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ibero.demo.entity.Rol;

@Repository
public interface IRolUserDao extends JpaRepository<Rol, Integer>{

	@Query(value = "SELECT * FROM roles WHERE level_user = :level", nativeQuery = true)
    Rol findByLevelUser(@Param("level") String level);
}
