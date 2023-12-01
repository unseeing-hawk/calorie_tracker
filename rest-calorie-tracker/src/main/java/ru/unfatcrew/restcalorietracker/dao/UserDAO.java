package ru.unfatcrew.restcalorietracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    User findByLogin(String login);

    User findByIdAndLogin(long id, String login);
}
