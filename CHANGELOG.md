# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 2.0.2

### Added
- Data Cloak: component
- Logger: Add support for Data Cloak obscurer
- Webserver: Add obscurer as route data to protect sensitive data
- Serdes: Support hiccup serialization
- Webser: Support `text/html` encoding with hiccup

### Changed:
- CI: get version from git tag

### Removed:
- Global: remove all non-lib components

### Fixed:
- Webserver: server didn't respect `Accept` request header
- Deploy: source code wasn't being included

## [v1.3.5] - 2023-08-03

### Added
- [PR #24] Serdes component for content negotiation & format parsing
- [PR #24] HTTP Client component
- [PR #24] Testing component

### Changed
- [PR #24] Webserver: replace internal content negotiation by serdes component
- [PR #24] Webserver: use HTTP Client component for internal tests
- [PR #24] Test: Better way to manage test runner configurations for each kind of test
- [PR #24] Reduced labels to be more concise

## Fixed:
- [PR #24] Logger: huge improvement in test consistency

## [v1.0.0] - 2023-08-03

### Added

- [PR #22] Feature Flag component
- [PR #22] Local environment: unleash

### Changed
- [PR #22] change interface namespace name from `interface` to `core`
- [PR #22] bump up clojure dependencies version

## [v0.3.0] - 2023-07-27

### Added

- [PR #15] Webserver component
- [PR #15] Logger docs
- [PR #16] Auto labels for PRs

### Changed

- [PR #14] Log: add `trace` macro
- [PR #15] Test: use hard links to reference test configuration
- [PR #15] Update documentation
- [PR #16] Normalize readme naming (all uppercase)

#### Removed

- [PR #16](https://github.com/yuhrao/big-bang/pull/16) Auto changelog update

## V0.1.0 (2023-07-17)

### Added:

- [PR #07]: Logger component

### Changed:

- [PR #07]: Tests now includes `:dev` alias
- [PR #07]: Moved integration test project's dependencies to `:test` alias

### Removed:

- [PR #07]: Unnecessary .keep files
- [PR #07]: Unused dependencies for database component testing

### Fixed:

- [PR #07]: Integration test runner was also executing unit tests

## V0.0.0 (2023-07-14)

### Added:

- [PR #04] Database component: migration manager
- [PR #04] Database component: function to execute SQL statements
- [PR #04] CI: github actions to run tests in pull requests

### Changed:

- [PR #04] Test: better test isolation to run integration tests apart from unit tests

[//]: # (Versions)

[v0.2.1]: https://github.com/yuhrao/big-bang/compare/v0.2.0...v0.2.1
[v1.0.0]: https://github.com/yuhrao/big-bang/compare/v0.2.1...v1.0.0
[v1.1.0]: https://github.com/yuhrao/big-bang/compare/v1.0.0...v1.1.0
[v1.3.5]: https://github.com/yuhrao/big-bang/compare/v1.1.0...v1.3.5

[//]: # (PRS)

[PR #04]: https://github.com/yuhrao/big-bang/pull/4
[PR #07]: https://github.com/yuhrao/big-bang/pull/7
[PR #14]: https://github.com/yuhrao/big-bang/pull/14
[PR #15]: https://github.com/yuhrao/big-bang/pull/15
[PR #16]: https://github.com/yuhrao/big-bang/pull/16
[PR #22]: https://github.com/no-code-no-bugs/big-bang/pull/22
[PR #24]: https://github.com/no-code-no-bugs/big-bang/pull/24
[PR #25]: https://github.com/no-code-no-bugs/big-bang/pull/25
