# Contributing to Estimator

First off, thank you for considering contributing to Estimator! We appreciate your time and effort in helping improve our project. The following is a set of guidelines for contributing to Estimator.

## How Can I Contribute?

### Reporting Bugs

If you encounter a bug, please open an issue on [GitHub](https://github.com/diasel2000/estimator/issues) and provide detailed information about the problem. Include the steps to reproduce the bug, the expected result, and the actual result.

### Suggesting Features

We welcome feature suggestions! If you have an idea for a new feature, please open an issue on [GitHub](https://github.com/diasel2000/estimator/issues) and describe the feature in detail. Explain why it would be useful and how it should work.

### Contributing Code

1. **Fork the Repository**: Click on the "Fork" button on the top right of the repository page and clone your fork to your local machine.

    ```sh
    git clone https://github.com/yourusername/estimator.git
    cd estimator
    ```

2. **Create a Branch**: Create a new branch for your feature or bug fix.

    ```sh
    git checkout -b feature-name
    ```

3. **Make Changes**: Implement your changes in the new branch.

4. **Write Tests**: Ensure that your changes are well-tested. We use JUnit for unit testing.

5. **Commit Changes**: Commit your changes with a clear and concise commit message.

    ```sh
    git commit -m "Add feature X"
    ```

6. **Push to GitHub**: Push your changes to your forked repository on GitHub.

    ```sh
    git push origin feature-name
    ```

7. **Create a Pull Request**: Open a pull request on the original repository and provide a detailed description of your changes. Include any relevant issue numbers.

### Code Style

Please follow the established code style guidelines for the project. This helps maintain consistency and readability across the codebase.

- Use meaningful variable and function names.
- Write comments to explain complex logic.
- Format your code using an appropriate style guide.

### Running Tests

Before submitting your code, ensure all tests pass successfully. You can run tests using Gradle:

```sh
./gradlew test
