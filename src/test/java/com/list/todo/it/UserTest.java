/*
package com.list.todo.it;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yml")
@Sql(value = {"/create-user-before.sql", "/create-todolists-before.sql", "/create-shares-before.sql", "/create-followers-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-user-after.sql", "/create-todolists-after.sql", "/create-shares-after.sql", "/create-followers-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails(value = "stepanich")
public class UserTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void userInfo() throws Exception {
        this.mockMvc.perform(get("/api/users/me"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(jsonPath("name").value("Stepan Matveev"))
                .andExpect(jsonPath("username").value("stepanich"))
                .andExpect(jsonPath("email").value("stepa.matv72@gmail.com"))
                .andExpect(status().isOk());

    }

    @Test
    public void serchUsersByUsername() throws Exception {
        this.mockMvc.perform(get("/api/users/search?username={username}", "ann"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(jsonPath("$").isArray())
                .andExpect(content().json("[\"anna\"]"))
                .andExpect(status().isOk());

    }

    @Test
    public void serchUsersByNonExistentUsername() throws Exception {
        this.mockMvc.perform(get("/api/users/search?username={username}", "kkkkk"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(status().isOk());

    }

    @Test
    public void getUserStats() throws Exception {
        this.mockMvc.perform(get("/api/users/userStats"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(jsonPath("$.myTodoLists[0].id").value("4"))
                .andExpect(jsonPath("$.myTodoLists[1].id").value("5"))
                .andExpect(jsonPath("$.sharedTodoLists[0].id").value("6"))
                .andExpect(jsonPath("$.sharedTodoLists[1].id").value("7"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUser() throws Exception {
        this.mockMvc.perform(put("/api/users/editProfile").content("{\n" +
                "	\"name\": \"Stepa Baklagan\",\n" +
                "	\"username\": \"stepka\",\n" +
                "	\"email\": \"stepa.matv72@gmail.com\",\n" +
                "	\"password\": \"Secrett\"\n" +
                "}").contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("name").value("Stepa Baklagan"))
                .andExpect(jsonPath("username").value("stepka"))
                */
/*.andExpect(jsonPath("email").value("stepa.matv72@gmail.com"))
                .andExpect(jsonPath("password").value("Secrett"))*//*

                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser() throws Exception {
        this.mockMvc.perform(delete("/api/users/deleteProfile"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isNoContent());
    }

    @Test
    public void followUser() throws Exception {
        this.mockMvc.perform(post("/api/users/followUser?username={username}", "anna"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("message").value("You'll follow this user!"))
                .andExpect(status().isOk());
    }

    @Test
    public void followNonExistentUser() throws Exception {
        this.mockMvc.perform(post("/api/users/followUser?username={username}", "annaanna"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("message").value("You can't follow this user!"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getFollowers() throws Exception {
        this.mockMvc.perform(get("/api/users/followers"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(jsonPath("$[0].username").value("vitaliy"))
                .andExpect(status().isOk());
    }

}
*/
