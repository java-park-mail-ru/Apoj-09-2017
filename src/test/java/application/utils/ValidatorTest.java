package application.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ValidatorTest {
    @Test
    public void testWrongLogin() {
        Assert.assertTrue(Validator.checkLogin("").contains(Validator.EMPTY_LOGIN));
        Assert.assertTrue(Validator.checkLogin("123456789012345678901234567890123456789012345678901234567").contains(Validator.LONG_LOGIN));
        Assert.assertTrue(Validator.checkLogin("12").contains(Validator.SHORT_LOGIN));
        Assert.assertTrue(Validator.checkLogin("AYE@#$%").contains(Validator.LOGIN_ERROR));
    }

    @Test
    public void testWrongEmail() {
        Assert.assertTrue(Validator.checkEmail("").contains(Validator.EMPTY_EMAIL));
        Assert.assertTrue(Validator.checkEmail("misha").contains(Validator.EMAIL_ERROR));
    }

    @Test
    public void testWrongPassword() {
        Assert.assertTrue(Validator.checkPassword("").contains(Validator.EMPTY_PASSWORD));
        Assert.assertTrue(Validator.checkPassword("12").contains(Validator.SHORT_PASSWORD));
        Assert.assertTrue(Validator.checkPassword("1234567111111111111111111111111111111111").contains(Validator.LONG_PASSWORD));
    }

}