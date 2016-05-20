---
layout: default
title: {{ site.name }}
---

# Introduction

This short workshop will guide you through creating a Gradle build file for an existing Java application, including building a distributable package and running integration tests.

# Setting up

This workshop is based on Eclipse and uses the Buildship plugin which provides support for developing Gradle projects. When writing this workshop, I used Eclipse Mars 2 (4.5.2).

## Installing buildship

* Go to **Help** > **Eclipse Marketplace...**

  ![Eclipse marketplace menu][pic-installsoftware]
  
* Search for "**buildship**"

* Click the **Install** button

  ![Buildship entry][pic-buildship]
  
* Continue through the wizard, accepting the license

* Restart Eclipse when prompted

## Cloning the workshop

* Click the Open perspective button

  ![Open perspective button][pic-open-perspective]
  
* Select the **Git** perspective

* On the Git Repositories view, click the button to **Clone a Git Repository**

  ![Clone repository button][pic-clone]
  
* Enter **https://github.com/Azquelt/gradle-workshop.git** as the URI and click **Next >**

  * _Note: you don't need to specify any credentials_

* Make sure that the **master** branch is selected (the other branches don't matter either way) and click **Next >**

* Choose a directory to store the checked out code and click **Finish**.

  * _Note: this should **not** be in your eclipse workspace_
  
  * _Note: do not select the option to import existing Eclipse projects as it does not work for Gradle projects_

## Importing the project

Unfortunately, eclipse won't give you the option of importing a repository as a gradle project after cloning it, so we have to do it manually.

* Go to **File** > **Import** and select **Gradle** > **Gradle Project**

* Click **Next >** on the welcome page

* Set the Project root directory to the directory you used when cloning the git repository and click **Next >**

* This project does not have a gradle wrapper, so set the gradle distribution to **Sepcific Gradle version** and choose **2.13**

* Set the Java home directory to where your Java SDK is installed

* Click **Next >**

* Wait for buildship to finish downloading Gradle and reading your build file and click **Finish**

On the Gradle Tasks view, open **gradle-workshop** > **build** and double-click **build**.
In the console view, you should now see the build failing.

_Note: if you can't see the Gradle Tasks view, you can open it by going to **Window** > **Show View** > **Other...** > **Gradle** > **Gradle Tasks**_

# Adding dependencies

Our build does not run because our application requires some dependencies which aren't declared in the build.gradle file.

* The application requires [parboiled][parboiled] 1.1.7
* The unit tests additionally require JUnit 4.12

We're going to fetch our dependencies from [maven central][maven central]. If you go to the page of the artifact you want to include,
it will helpfully give you a line to add to your gradle file to add the artifact as a dependency of your main code.

Go back to the Java perspective, open `build.gradle` and add the following code:

```gradle
repositories {
	mavenCentral()
}

dependencies {
	compile 'org.parboiled:parboiled-java:1.1.7'

	testCompile 'junit:junit:4.12'
}
```

Note that we use `compile` for dependencies of our application and `testCompile` for dependencies of our unit tests.
Gradle calls these groupings of dependencies **configurations**. A list of configurations added by the Java plugin and how they're used can be found [in the user guide][java configurations].

At this point, even though we've added the dependencies to the `build.gradle` file, Eclipse is still showing compile error markers on our code. To fix this, right-click on the **gradle-workshop** project > **Gradle** > **Refresh Gradle project**. After a few moments while Gradle processes the project and downloads the dependencies, Eclipse should show our project as being error-free.

Now go to the **Gradle Tasks** view again, open **gradle-workshop** > **build** and double-click **build**. This time you should see the build complete successfully.

But where's our output? Here again, the user guide will tell us that java artifacts are built in build/lib, but by default this folder is hidden in the project explorer in Eclipse.

Click the down arrow in the Project Explorer view and select **Customize view...** (or **Filters...** in the Package Explorer view), uncheck **Gradle build folder** and click **OK**.

Now select the **gradle-workshop** project and press **F5** and the jar should show up under `build/libs`.

# Packaging an application

We will now build a user-friendly package for our application so that a user can download a single archive, unpack it and run like a native application.

Fortunately, Gradle's Application plugin does most of the work for us here including creating .bat and shell scripts to launch java with the correct classpath to run our app and creating an archive which includes all our library dependencies.

The [Application plugin page][application plugin] in the user guide tells us that we must apply the plugin and set the `mainClassName` property.

In `build.gradle`, replace the top section of the file with this:


```gradle
apply plugin: 'java'
apply plugin: 'application'

mainClassName = 'uk.co.azquelt.simplecalc.Main'
```

From the Gradle tasks view, build the project again.

Select the project and press **F5** and you should now see `build/distributions/gradle-workshop.zip` which contains a zipped up directory with a script to run the application.

Although gradle-workshop is a descriptive name for the project, it's not a great name for the built application so let's change that. The [application plugin page][application plugin] notes that it adds some properties which we can find on the [Project API][api application properties]. This tells us that there is an `applicationName` property which we can set. Similarly, on the [Java Plugin page][java properties] we find the `archivesBaseName` property which will control the name of our compiled jar.

Set both of these properties in the `gradle.build` file:


```gradle
applicationName = 'simple-calculator'
archivesBaseName = applicationName
```

Build the project again, and we see that everything is now named `simple-calculator` instead of `gradle-workshop`

# Running integration tests

Our project has some integration tests under `src/integrationTest/java`. You might not have noticed them because
they're not mentioned in the `build.gradle` file and don't show up as a Java source folder. Let's fix that and make sure they get run when we do our build.

## Adding a source set

First, we need to tell gradle about the source code. The Java plugin has support for additional [source sets][java source sets] and will automatically add new tasks and configurations (remember these from earlier?)
for each new source set that you add. These are explained further down in the [tasks][java tasks] and [dependency management][java configurations] sections of the user guide.

Add this section near the top of the build.gradle file. It needs to come before the `dependencies` block.

```gradle
sourceSets {
	integrationTest
}
```

We also need to add the same JUnit dependency to the integrationTestCompile configuration:


```gradle
dependencies {
	compile 'org.parboiled:parboiled-java:1.1.7'

	testCompile 'junit:junit:4.12'

	integrationTestCompile 'junit:junit:4.12'
}
```

Now right-click on the project > **Gradle** > **Refresh Gradle project** and you should see the new integrationTest source folder appear.

![IntegrationTest source folder][pic-integrationtest]

## Running the tests

However, if we run the `build` task again, we don't see the integration tests mentioned at all! This is because although we have some new tasks, they're not in the task graph so they can't be executed by the build task.

Let's have a look at the task graph for the Java plugin (in green) taken from the [user guide][java tasks], along with the new tasks that have been added for our source set (in orange).

![Task graph for Java plugin next to tasks for integration tests](images/tasks1.svg)

We can clearly see in this diagram that nothing depends on the new tasks so they will never be run unless we request them directly. We need to add them to the existing task graph created by the Java plugin but the next question is where should they go?

Well, the only time we need to build the integration tests is when we're going to run the integration tests so they should be a dependency of the task that runs the tests.

But wait, we don't have a task that runs the tests yet! We need to create this new task and then we can link all of our tasks into the task graph like this:

![Integration test tasks integrated into task graph](images/tasks2.svg)

Here's the snippet you need to add to your `build.gradle` file to create the `integrationTest` task:


```gradle
task integrationTest(type: Test) {
	dependsOn integrationTestClasses
	dependsOn installDist

	testClassesDir = sourceSets.integrationTest.output.classesDir
	classpath = sourceSets.integrationTest.runtimeClasspath
}
```

Let's break down what's going on here.

Using the `task` keyword, we're creating a new task named "integrationTest" and we're using the `Test` task type because we want to run JUnit tests.

We then need to configure the test task using the methods and properties listed in the [Test task type DSL documentation][dsl test task type]. Here the essential settings are `testClassesDir` to tell the task where our tests are and `classpath` to define the classpath needed to run the tests.

We could define both of these manually but it's easier to pull this information from the source set which we created earlier because Gradle knows where the class files are and has computed the classpath from the `integrationTestCompile` dependencies.

We look up the source set by name from the [sourceSets property][dsl sourceSets property] and can then call any of the methods on the [SourceSet interface][api sourceset interface] to get the information we want.

We also set up the task dependencies needed for our `integrationTest` task to make sure that everything it needs to run is available. The things we need are:

* the compiled test classes

  * these will be produced by the `integrationTestClasses` task, added automatically by the Java plugin when we added the `integrationTest` sourceSet
  
  * the tasks added by the Java plugin are listed in the [user guide][java tasks]
  
* the built application, so that the tests can run it

  * the `installDist` task, added by the Application plugin will do this for us, extracting the distribution archive to `build/install/applicationName`
  
  * the tasks added by the Application plugin are listed in the [user guide][application tasks]

Lastly, to get our task graph looking the way we want it, we also need to have the `check` task depend on our new `integrationTest` task, which we can do by adding this line.


```gradle
check.dependsOn integrationTest
```

Here we're configuring a task which has already been created by the plugin. If we wanted to add more configuration options, we could also use the form


```gradle
check {
    dependsOn integrationTest
    /* ... more options ... */
}
```

It's important to note that when we're creating a *new* task, we use the `task` keyword, but when we're configuring an *existing* task we don't.

Now run the `build` task again, and you should see it build and run the all the tests.

_Note: if your integration tests don't pass, first check that the name of your application (which you specified earlier) matches the application name in the **RunApplication.java** file._

# Further information

This is the end of the guided workshop, we've successfully created a Gradle build file to build our application into a distributable package and run the associated unit and integration tests.

Here are some possible projects if you'd like to explore Gradle further:

* Update the build script so that it always runs the unit tests before the integration tests

* Create a [Gradle wrapper][wrapper plugin] for your project so that developers don't have to have Gradle set up to build your project

* Version your application and deploy it to a maven repository

* Build a Java EE app using the [War][war plugin] and [Ear][ear plugin] plugins

* Use the [Liberty plugin][liberty plugin] to deploy your app to a Liberty server

* Write integration tests and have your build start the server, deploy your app, run the tests and then clean up after itself

* Turn your integration test tasks into a plugin with its own configuration block so it can easily be applied to your next Java EE project (don't forget to document it so you remember how it works)

Here are some resources that you may find useful:

* [Gradle User Guide][gradle user guide]

* [Groovy Language Documentation][groovy language]

* [Groovy extensions to standard Java classes][groovy jdk extensions]

[java properties]: https://docs.gradle.org/current/userguide/java_plugin.html#N1529B
[java source sets]: https://docs.gradle.org/current/userguide/java_plugin.html#N14E7A
[java configurations]: https://docs.gradle.org/current/userguide/java_plugin.html#sec:java_plugin_and_dependency_management
[java tasks]: https://docs.gradle.org/current/userguide/java_plugin.html#N14E92
[application plugin]: https://docs.gradle.org/current/userguide/application_plugin.html
[application tasks]: https://docs.gradle.org/current/userguide/application_plugin.html#N15F3F
[api sourceset interface]: https://docs.gradle.org/current/javadoc/org/gradle/api/tasks/SourceSet.html
[dsl sourceSets property]: https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#org.gradle.api.Project:sourceSets
[dsl test task type]: https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.Test.html
[parboiled]: http://parboiled.org
[maven central]: http://search.maven.org/
[api application properties]: https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#N14431
[wrapper plugin]: https://docs.gradle.org/current/userguide/wrapper_plugin.html
[war plugin]: https://docs.gradle.org/current/userguide/war_plugin.html
[ear plugin]: https://docs.gradle.org/current/userguide/ear_plugin.html
[liberty plugin]: https://github.com/WASdev/ci.gradle

[gradle user guide]: https://docs.gradle.org/current/userguide/userguide.html
[groovy language]: http://www.groovy-lang.org/documentation.html
[groovy jdk extensions]: http://www.groovy-lang.org/gdk.html

[pic-installsoftware]: images/01-installsoftware.png
[pic-buildship]: images/02-buildship.png
[pic-open-perspective]: images/03-open-perspective.png
[pic-clone]: images/04-clone.png
[pic-integrationtest]: images/10-integrationTest.png