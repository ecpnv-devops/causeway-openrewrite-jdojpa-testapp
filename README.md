# causeway-openrewrite-jdojpa-testapp

This is a test application for the Causeway OpenRewrite JDO/JPA recipe.

The recipe itself can be found in the [jdojpa](https://github.com/ecpnv-devops/jdojpa) repo.

## Prereqs to build

set up a personal access token for a user with `read:packages` permissions to the `ecpnv-devops` org.

In the github actions' secrets, define the following:

* `READ_PACKAGES_USERNAME` - the username of the user with the personal access token
* `READ_PACKAGES_TOKEN` - the value of the personal access token


## To run the recipe locally

* export the following environment variables:

  ```bash
  export READ_PACKAGES_USERNAME=<username>
  export READ_PACKAGES_TOKEN=<token>
  ```

* then run using:

  ```bash
  mvn rewrite:run -s .m2/settings.xml
  ```

## To build in github action




