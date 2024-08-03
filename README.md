
# Atipera

## Table of Contents
- [Introduction](#introduction)
- [Installation](#installation)
- [Usage](#usage)
- [Dependencies](#dependencies)
- [Configuration](#configuration)
- [Contributors](#contributors)
- [Possible Improvements](#improvements)
- [License](#license)

## Introduction
Atipera is a Java-based project that utilizes Spring Boot to create a RESTful web service. The project provides an endpoint for retrieving Github users' public repositories and their branches provided by GithubAPI.

## Installation
To install and set up the project, follow these steps:
1. Clone the repository:
    ```bash
    git clone https://github.com/Rybaczek/atipera.git
    ```
2. Navigate to the project directory:
    ```bash
    cd atipera
    ```
3. Build the project using Maven:
    ```bash
    ./mvnw clean install
    ```

## Usage
To run the project, use the following command:
```bash
./mvnw spring-boot:run
``` 
The application will start on http://localhost:8080.
## API Endpoints

The project provides a RESTful endpoint:

`GET /users/{username}/repositories` - Retrieves a list of user's GitHub repositories that are not forks.

Example:
```bash
    curl -X GET http://localhost:8080/users/Rybaczek/repositories ^
    -H "Accept: application/json"
```
Example succesful response:
```json
{
  "githubRepositories": [
    {
      "repositoryName": "gamemasters-organizer-bestiary-and-location-interactive-navigator",
      "ownerLogin": "Rybaczek",
      "branches": [
        {
          "name": "master",
          "sha": "37686a68b41b24f00090a1b6bfc3709dedeca459"
        }
      ]
    },
    {
      "repositoryName": "isaac-character-generator-application",
      "ownerLogin": "Rybaczek",
      "branches": [
        {
          "name": "PullRequest",
          "sha": "53484d6eb7ecf2a0a030d2279b2a45a642db96e0"
        },
        {
          "name": "master",
          "sha": "bd844dfefb6319e8d0e34b9892812387933bd6d5"
        }
      ]
    }
  ]
}
```
Example 404 response
```json
{
  "responseCode": 404,
  "message": "Branch from: repository-with-non-existing-branch repository owned by: Rybaczek not found"
}
```

## Dependencies
Java 21

## Contributors

    Dawid Rybak

## Possible Improvements
This application can be further improved with:
- Usage of resilience4j library for managing request timeouts and retries
- Adding pagination to ensure retrieving all possible repositories in case of users with a large number of repositories exceeding the GitHubAPI default number of records per page
- Adding authorization for GitHubAPI to use applications beyond the limit of requests for unauthorized users

## License
This project is licensed under the MIT License. See the LICENSE file for more details.