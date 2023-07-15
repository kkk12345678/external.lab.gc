package org.example.gc.ut.service;

import org.example.gc.dto.UserDto;
import org.example.gc.dto.UserSignupDto;
import org.example.gc.entity.Role;
import org.example.gc.entity.User;
import org.example.gc.exception.AlreadyExistsException;

import org.example.gc.parameters.UserParameters;
import org.example.gc.repository.RoleRepository;
import org.example.gc.repository.UserRepository;
import org.example.gc.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {
    private static final Role ROLE_ADMIN = new Role(1L, "admin");
    private static final Role ROLE_USER = new Role(2L, "user");
    private static final List<User> users = List.of(
            new User(1L, "admin", "1", ROLE_ADMIN),
            new User(2L, "user1", "1", ROLE_USER),
            new User(3L, "user2", "1", ROLE_USER)
    );
    private static final List<UserDto> emptyList = new ArrayList<>();
    private static final long ID = 4L;
    private static final String NAME = "user3";
    private static final String PASSWORD = "1";
    private static final User user = new User(ID, NAME, PASSWORD, ROLE_USER);

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void testGetAllWithoutPagination() {
        UserParameters userParameters = new UserParameters();
        Mockito.when(userRepository.getAll(userParameters)).thenReturn(users);
        assertEquals(users.stream().map(User::toDto).collect(Collectors.toList()), userService.getAll(userParameters));
    }

    @Test
    void testGetAllWitValidPagination() {
        UserParameters userParameters = new UserParameters();
        userParameters.setLimit(1);
        userParameters.setPage(2);
        Mockito.when(userRepository.getAll(userParameters)).thenReturn(List.of(users.get(1)));
        assertEquals(List.of(users.get(1).toDto()), userService.getAll(userParameters));
    }

    @Test
    void testGetAllWitInvalidLimit() {
        UserParameters userParameters = new UserParameters();
        userParameters.setLimit(-1);
        assertEquals(emptyList, userService.getAll(userParameters));
        userParameters.setLimit(0);
        assertEquals(emptyList, userService.getAll(userParameters));
    }
/*
    @Test
    void testAddNewTag() {
        user.setId(null);
        Mockito.when(userRepository.getByName(NAME)).thenReturn(null);
        Mockito.when(userRepository.insertOrUpdate(user)).thenReturn(user);
        UserSignupDto dto = new UserSignupDto();
        dto.setName(NAME);
        dto.setPassword(PASSWORD);
        Mockito.when(passwordEncoder.encode(PASSWORD)).thenReturn(new BCryptPasswordEncoder().encode("1"));
        Mockito.when(roleRepository.getByName("user")).thenReturn(ROLE_USER);
        user.setId(ID);
        assertEquals(user.toDto(), userService.add(dto));
        user.setId(ID);
    }
/*
    @Test
    void testAddWhenExists() {
        TagDto dto = new TagDto(NAME);
        Mockito.when(tagRepository.getByName(NAME)).thenReturn(tag);
        assertThrows(AlreadyExistsException.class, () -> tagService.add(dto));
    }
/*
    @Test
    void testRemoveWhenExists() {
        Mockito.when(tagRepository.getById(ID)).thenReturn(tag);
        Mockito.doNothing().when(tagRepository).delete(tag);
        assertDoesNotThrow(() -> tagService.remove(ID));
    }

    @Test
    void testRemoveWhenDoesNotExist() {
        Mockito.when(tagRepository.getById(ID)).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> tagService.remove(ID));
    }

    @Test
    void testGetByIdWhenExists() {

        Mockito.when(tagRepository.getById(ID)).thenReturn(tag);
        assertEquals(tag, tagService.getById(ID));
    }

    @Test
    void testGetByIdWhenDoesNotExist() {
        Mockito.when(tagRepository.getById(ID)).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> tagService.getById(ID));
    }

    @Test
    void testGetMostValuable() {
        Tag tag = new Tag(ID, NAME);
        Mockito.when(tagRepository.getMostValuable()).thenReturn(tag);
        assertEquals(tag, tagService.getMostValuable());
    }

 */
}
