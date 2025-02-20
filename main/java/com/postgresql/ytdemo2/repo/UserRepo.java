package com.postgresql.ytdemo2.repo;

import com.postgresql.ytdemo2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface UserRepo  extends JpaRepository<User,Long> {

    User findByUsername(String username);



}
