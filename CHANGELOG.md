# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v0.2.1] - 2023-07-20
### :wrench: Chores
- [`c304f42`](https://github.com/yuhrao/big-bang/commit/c304f42562e284e487eb2a997e4d9e3644c9d485) - add changelog *(commit by [@yuhrao](https://github.com/yuhrao))*

## V0.1.0 (2023-07-17)

## Added:
- [PR #7](https://github.com/yuhrao/big-bang/pull/7): Logger component

## Changed:
- [PR #7](https://github.com/yuhrao/big-bang/pull/7): Tests now includes `:dev` alias
- [PR #7](https://github.com/yuhrao/big-bang/pull/7): Moved integration test project's dependencies to `:test` alias

## Removed:
- [PR #7](https://github.com/yuhrao/big-bang/pull/7): Unnecessary .keep files
- [PR #7](https://github.com/yuhrao/big-bang/pull/7): Unused dependencies for database component testing

## Fixed:
- [PR #7](https://github.com/yuhrao/big-bang/pull/7): Integration test runner was also executing unit tests

## V0.0.0 (2023-07-14)

## Added:
- [PR #4](https://github.com/yuhrao/big-bang/pull/4) Database component: migration manager
- [PR #4](https://github.com/yuhrao/big-bang/pull/4) Database component: function to execute SQL statements
- [PR #4](https://github.com/yuhrao/big-bang/pull/4) CI: github actions to run tests in pull requests

## Changed:
- [PR #4](https://github.com/yuhrao/big-bang/pull/4) Test: better test isolation to run integration tests apart from unit tests

[v0.2.1]: https://github.com/yuhrao/big-bang/compare/v0.2.0...v0.2.1
