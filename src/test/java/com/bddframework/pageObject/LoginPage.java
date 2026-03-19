package com.bddframework.pageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    private WebDriver driver;

    @FindBy(id="customer_email")
    WebElement usernameFiled;

    @FindBy(id="customer_password")
    WebElement passwordField;

    @FindBy(xpath="input[@value='Sign In']")
    WebElement signInBtn;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    public LoginPage serUsernameFiled(String userName) {
         usernameFiled.sendKeys(userName);
         return new LoginPage(driver);
    }

    public LoginPage getPasswordField(String password) {
         passwordField.sendKeys(password);
        return new LoginPage(driver);
    }

    public WebElement getSignInBtn() {
        return signInBtn;
    }
}
