package com.bddframework.pageObject;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    private WebDriver driver;

    @FindBys({
            @FindBy(id = "customer_login_link"),
            @FindBy(tagName = "a")
    })
    WebElement loginLink;

    @FindAll({
            @FindBy(id = "customer_register_link"),
            @FindBy(tagName = "a")
    })
    WebElement signUpLink;

    public HomePage(WebDriver driver) {
     this.driver=driver;
        PageFactory.initElements(driver,this);
    }

    public LoginPage clickOnLogin() {
         loginLink.click();
         return new LoginPage(driver);
    }

    public SignUpPage clickOnSignUp() {
        signUpLink.click();
        return new SignUpPage();
    }
}
