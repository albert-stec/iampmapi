package com.stecalbert.iampmapi.service;

import com.stecalbert.iampmapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    Map<Long, User> map  = new HashMap<Long, User>() {{
        put(1L, new User(1L, "Albert", "Stec", "stecalbert@gmail.com", true, "DEV"));
        put(2L,  new User(2L, "Marcin", "Kowalksi", "marcin@gmail.com", true, "HR"));
        put(3L,  new User(3L, "Jan", "Markus", "markus@gmail.com", true, "TEST"));
    }};


    public List<User> findAll() {
        return map.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    public void create(User user) {
        map.put(user.getId(), user);
    }

    public void deleteAll() {
        map.clear();
    }
}
