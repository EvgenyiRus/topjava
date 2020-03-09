package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

@ActiveProfiles(Profiles.REPOSITORY_IMPLEMENTATION_JPA)
public class UserServiceJpaTest extends AbstractUserServiceTest {
}
