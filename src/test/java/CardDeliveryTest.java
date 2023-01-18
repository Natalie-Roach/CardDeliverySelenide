import org.junit.jupiter.api.Test;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardDeliveryTest {

    @BeforeEach
    void openWeb() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @Test
    void shouldBeSuccessful() {

        $("[data-test-id='city'] input").setValue("Нижний Новгород");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("Анна-Мария Петрова");
        $("[data-test-id='phone'] input").setValue("+79162003040");
        $("[data-test-id='agreement']").click();
        $x("//*[@class=\"button__text\"]").click();
        $("[data-test-id='notification']").shouldHave(Condition.text("Успешно! Встреча успешно забронирована на " + verificationDate), Duration.ofMillis(15000));
    }

    @Test
        //город, не входящий в список административных центров
    void shouldBeOutOfListCity() {

        $("[data-test-id='city'] input").setValue("Сызрань");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("Анна-Мария Петрова");
        $("[data-test-id='phone'] input").setValue("+79162003040");
        $("[data-test-id='agreement']").click();
        $x("//*[@class=\"button__text\"]").click();

        String text = $x("//*[@data-test-id=\"city\"]").getText();
        assertEquals("Доставка в выбранный город недоступна", text);
    }

    @Test
        //пустое поле Город
    void shouldBeEmptyCityField() {

        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("Анна-Мария Петрова");
        $("[data-test-id='phone'] input").setValue("+79162003040");
        $("[data-test-id='agreement']").click();
        $x("//*[@class=\"button__text\"]").click();

        String text = $x("//*[@data-test-id=\"city\"]").getText();
        assertEquals("Поле обязательно для заполнения", text);

    }

    @Test
        //город латиницей
    void shouldBeInvalidCityName() {
        $("[data-test-id='city'] input").setValue("Moscow");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("Александр Македонский");
        $("[data-test-id='phone'] input").setValue("+79162003040");
        $("[data-test-id='agreement']").click();
        $x("//*[@class=\"button__text\"]").click();

        String text = $x("//*[@data-test-id=\"city\"]").getText();
        assertEquals("Доставка в выбранный город недоступна", text);
    }


    @Test
        //имя и фамилия латиницей
    void shouldBeInvalidName() {
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("Maria Lopez");
        $("[data-test-id='phone'] input").setValue("+79162003040");
        $("[data-test-id='agreement']").click();
        $x("//*[@class=\"button__text\"]").click();

        String text = $x("//*[@data-test-id=\"name\"]").getText();
        assertEquals("Фамилия и имя\n" + "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);

    }

    @Test
        //имя и фамилия - пустое поле
    void shouldBeEmptyNameField() {
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue("+79162003040");
        $("[data-test-id='agreement']").click();
        $x("//*[@class=\"button__text\"]").click();

        String text = $x("//*[@data-test-id=\"name\"]").getText();
        assertEquals("Фамилия и имя\n" + "Поле обязательно для заполнения", text);

    }

    @Test
        //пустое поле Дата
    void shouldBeEmptyDateField() {
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().format(DateTimeFormatter.ofPattern(""));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("Иммануил Кант");
        $("[data-test-id='phone'] input").setValue("+79162003040");
        $("[data-test-id='agreement']").click();
        $x("//*[@class=\"button__text\"]").click();

        String text = $x("//*[contains(text(),'Неверно введена дата')]").getText();
        assertEquals("Неверно введена дата", text);
    }

    @Test
        //неверная дата
    void shouldBeInvalidDate() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79162003040");
        $("[data-test-id='agreement']").click();
        $x("//*[@class=\"button__text\"]").click();

        String text = $x("//*[contains(text(),'Заказ на выбранную дату невозможен')]").getText();
        assertEquals("Заказ на выбранную дату невозможен", text);

    }

    @Test
        //пустое поле Телефон
    void shouldBeEmptyPhoneNumberField() {
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("Иммануил Кант");
        $("[data-test-id='phone'] input").setValue("");
        $("[data-test-id='agreement']").click();
        $x("//*[@class=\"button__text\"]").click();

        String text = $x("//*[@data-test-id=\"phone\"]").getText();
        assertEquals("Мобильный телефон\n" + "Поле обязательно для заполнения", text);

    }

    @Test
        //телефон без +7, но с 8
    void shouldBeInvalidPhoneNumber() {
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("Иммануил Кант");
        $("[data-test-id='phone'] input").setValue("89102345678");
        $("[data-test-id='agreement']").click();
        $x("//*[@class=\"button__text\"]").click();

        String text = $x("//*[@data-test-id=\"phone\"]").getText();
        assertEquals("Мобильный телефон\n" + "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);

    }

    @Test
        //номер телефона длиннее чем 11 символов
    void shouldBeLongerPhoneNumber() {
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("Иммануил Кант");
        $("[data-test-id='phone'] input").setValue("+7910234567890");
        $("[data-test-id='agreement']").click();
        $x("//*[@class=\"button__text\"]").click();

        String text = $x("//*[@data-test-id=\"phone\"]").getText();
        assertEquals("Мобильный телефон\n" + "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
        //номер телефона короче, чем 11 символов
    void shouldBeShorterPhoneNumber() {
        $("[data-test-id='city'] input").setValue("Калининград");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("Иммануил Кант");
        $("[data-test-id='phone'] input").setValue("+791021016");
        $("[data-test-id='agreement']").click();
        $x("//*[@class=\"button__text\"]").click();

        String text = $x("//*[@data-test-id=\"phone\"]").getText();
        assertEquals("Мобильный телефон\n" + "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
        //без галочки в checkbox
    void shouldNotBeApproval() {
        $("[data-test-id='city'] input").setValue("Нижний Новгород");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.DELETE);
        String verificationDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id='date'] input").setValue(verificationDate);
        $("[data-test-id='name'] input").setValue("Дмитрий Пожарский");
        $("[data-test-id='phone'] input").setValue("+79162003040");
        $x("//*[@class=\"button__text\"]").click();

        String text = $x("//*[@data-test-id=\"agreement\"]").getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных", text);
    }

}

