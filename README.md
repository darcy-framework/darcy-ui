**darcy** is...
=========

* A framework for writing **page objects** in order to automate interaction with graphical user interfaces. Page objects are classes that model what a user can see and do. In Darcy each page, or subset of a page, is called a [View](https://github.com/darcy-framework/darcy/blob/master/src/main/java/com/redhat/darcy/ui/View.java).
* Automation library agnostic -- any library that can find UI elements and interact with them can work with Darcy. [Selenium WebDriver](https://code.google.com/p/selenium/) support is provided by [darcy-webdriver][3].
* Flexible and extendable by virtue of a declarative, **element-based DSL**. Write your page objects in terms of the UI buttons, labels, and widgets that you see. 
* Dependent on Java 8. [Get your lambda on](http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)!

**darcy** is not...
=========

* Web-specific by default. Extend the DSL to add web browser interaction with [darcy-web][4]. Darcy-webdriver is an implementation of this API with a WebDriver backend. Your web automation projects can simply include [darcy-webdriver][3] and get going.
* An automatic solution to every problem. It's still on you to architect your automation code maintainably. Darcy will help by solving a whole host of nasty UI-automation related challenges out of the box, so you can concentrate on the deeper and more interesting complexities specific to your automation project.


darcy's best friend: **synq**
=============================

[Synq][1] is a sister project to Darcy. Synq helps Darcy and your automation code sync up with external asynchronous processes (such as dynamic content on a web page). For folks coming from WebDriver, Synq gets you what WebDriverWait and ExpectedConditions get you, but with more flexibility (wait for multiple conditions, throw errors if other conditions are met in the meantime ... stuff like that). Synq is used heavily within Darcy itself, and so a lot of the explicit waits you might normally have to write in Selenium code will happen "automagically", behind the scenes. However, there are still plenty of things your automation code will need to wait for that Darcy has no other way of knowing about, so [read more here][1] to get to know Synq's API.

example page object
===================
```java
@RequireAll
public class MyHomePage extends AbstractView {
  TextInput login = textInput(By.id("login"));
  TextInput password = textInput(By.id("password"));
  Button submit = button(By.id("submit"));

  @NotRequired
  Label errorMsg = label(By.class("error"));

  public AccountDetails login(Credentials credentials) {
    login.clearAndType(credentials.login());
    password.clearAndType(credentials.password());
    
    return after(submit::click)
        .expect(transition().to(new AccountDetails())
        .failIf(errorMsg::isDisplayed)
          .throwing(new InvalidLoginException(credentials, errorMsg.readText()))
        .waitUpTo(1, MINUTES);
  }
}
```

getting started
===============

Check out the [getting started][5] tutorials to learn more about Darcy.


contributing
============

The darcy framework is very young, and there is a whole lot to do... Like write this section about contributing!

license
=======

**darcy** is licensed under [version 3 of the GPL][2].


  [1]: https://github.com/darcy-framework/synq
  [2]: https://www.gnu.org/copyleft/gpl.html
  [3]: https://github.com/darcy-framework/darcy-webdriver
  [4]: https://github.com/darcy-framework/darcy-web
  [5]: https://github.com/darcy-framework/darcy/wiki/Getting-Started-%231:-Darcy-Fundamentals
