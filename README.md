darcy-ui
=========
[![Build Status](https://travis-ci.org/darcy-framework/darcy-ui.svg?branch=master)](https://travis-ci.org/darcy-framework/darcy-ui) [![Coverage Status](https://coveralls.io/repos/darcy-framework/darcy-ui/badge.png?branch=master)](https://coveralls.io/r/darcy-framework/darcy-ui?branch=master)
[![Stories in Ready](https://badge.waffle.io/darcy-framework/darcy-ui.png?label=ready&title=Ready)](https://waffle.io/darcy-framework/darcy-ui)

**Darcy** is a framework for writing [**page objects**](http://martinfowler.com/bliki/PageObject.html) in order to automate interaction with graphical user interfaces. Page objects are classes that model what a user can see and do with a specific page.

Key features:

* Automation library agnostic -- any library that can find UI elements and interact with them can be wrapped with **darcy**. [Selenium WebDriver](https://code.google.com/p/selenium/) support is provided by [darcy-webdriver](https://github.com/darcy-framework/darcy-webdriver).
* Flexible and extendable by virtue of a declarative, **element-based DSL**. Write your page objects in terms of the UI buttons, labels, and widgets that you see. Abstract complicated interactions in reusable, custom element types with high-level APIs.
* Allows easy configuration of events and conditions to keep your automation code synchronized with the UI (or even data!) it is controlling. Dealing with AJAX, single page web apps, or backend data flows is no longer a pain point. Say goodbye to `Thread.sleep`!
* Open source and licensed under [version 3 of the GPL.](https://www.gnu.org/copyleft/gpl.html)
* Dependent on Java 8. [Get your lambda on!](http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)

**Darcy** is divided into modules. This module, darcy-ui, defines a general purpose API for all types of user interfaces. [darcy-web][4] extends the API for web browser automation.

example page object
===================
```java
import static com.redhat.darcy.ui.elements.Elements.textInput;
import static com.redhat.darcy.ui.elements.Elements.button;
import static com.redhat.synq.Synq.after;

@RequireAll
public class MyHomePage extends AbstractView {
  private TextInput login = textInput(By.id("login"));
  private TextInput password = textInput(By.id("password"));
  private Button submit = button(By.id("submit"));

  @NotRequired
  private Label errorMsg = label(By.className("error"));

  public AccountDetails login(Credentials credentials) {
    login.clearAndType(credentials.login());
    password.clearAndType(credentials.password());
    
    return after(submit::click)
        .expect(transition().to(new AccountDetails())
        .failIf(errorMsg::isDisplayed)
        .throwing(new InvalidLoginException(credentials, errorMsg::readText))
        .waitUpTo(1, MINUTES);
  }
}
```

getting started
===============

Check out the [Automating Applications with Darcy][5] GitBook to learn more about **darcy**.


contributing
============

Try it out and open an issue if you don't like something! Pull requests welcome and encouraged! Please read the documentation to get started, and check out the issue list if you're looking for something to do.

license
=======

**darcy** is licensed under [version 3 of the GPL][2].


  [1]: https://github.com/darcy-framework/synq
  [2]: https://www.gnu.org/copyleft/gpl.html
  [3]: https://github.com/darcy-framework/darcy-webdriver
  [4]: https://github.com/darcy-framework/darcy-web
  [5]: http://darcy-framework.gitbooks.io/automating-applications-with-darcy/
  [6]: http://martinfowler.com/bliki/PageObject.html
