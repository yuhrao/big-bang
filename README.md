The Polylith documentation can be found here:

- The [high-level documentation](https://polylith.gitbook.io/polylith)
- The [Polylith Tool documentation](https://github.com/polyfy/polylith)
- The [RealWorld example app documentation](https://github.com/furkan3ayraktar/clojure-polylith-realworld-example-app)

You can also get in touch with the Polylith Team via our [forum](https://polylith.freeflarum.com) or
on [Slack](https://clojurians.slack.com/archives/C013B7MQHJQ).

# big-bang

Where everything begins...

<!-- TOC -->

* [big-bang](#big-bang)
    * [Components](#components)
    * [3rd party components](#3rd-party-components)

<!-- TOC -->

## Components

| Name         | Description                                                                           |  
|--------------|---------------------------------------------------------------------------------------|
| Config       | Manage configurations. Get 'em from environment variables or system properties        |
| Database     | Interact with databases (mainly SQL) running migrations, executing DML and DQL in it. |
| Logger       | Logging features with normalizations and context.                                     |
| Webserver    | Run fully featured webservers with serialization/deserialization and swagger docs     |
| Feature Flag | Handle everything related with feature flags (uses Unleash)                           |
| Serdes       | Data Serialization and Deserialization                                                |
| HTTP Client  | Execute HTTP requests                                                                 |
| Data Cloak   | Obscure sensitive data                                                                |

## 3rd party components

- [Directus](https://github.com/directus/directus)
- [Unleash](https://github.com/Unleash/unleash)
- [Ory Kratos](https://www.ory.sh/docs/kratos/ory-kratos-intro)(Identity Management Platform)
- [Ory Oathkeeper](https://www.ory.sh/docs/oathkeeper/) (Authorization Layer for HTTP)
- [Rollbar](https://rollbar.com/): Track exceptions (clj lib: [rollcage](https://github.com/circleci/rollcage))

### Dev Stuff
Some dev stuff I'm using that's helping a lot to run everything locally.
- [MailSlurper](https://www.mailslurper.com/)

## Dev Scripts

## Run tests

```shell
# This is just a helper. Use it if you want.
alias poly='clojure -M:poly'

# unit testing
poly test project:development

# integration
poly test project:itests

# Test all
poly test :dev
```

## To Do

- [ ] Create API to get devices and brands
- [ ] Create PR in openmidi to fulfill missing device data
- [ ] Migrate JSON serde to Charred
- [ ] Write down tests for REST schemas (commons and specific)
