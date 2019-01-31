#### Setup Pact Broker in local with Ruby

- Install postgresql. Follow the instruction [here](https://github.com/DiUS/pact_broker-docker/blob/master/POSTGRESQL.md#installation-of-non-docker-postgresql).
- Create a PostgreSQL (recommended) or MySQL (not recommended, see following note) database.
    ```
    $ psql postgres
    > create database pact_broker;
    > CREATE USER pact_broker WITH PASSWORD 'pact_broker';
    > GRANT ALL PRIVILEGES ON DATABASE pact_broker to pact_broker;
    ```
- Install ruby 2.2.0 or later `brew install ruby` and bundler >= 1.12.0 `gem install bundler`
- `cd pact-broker-ruby`
- Modify the config.ru and Gemfile as desired (eg. choose database driver gem, set your database credentials. Use the "pg" gem if using Postgres.)
- Please ensure you use encoding: 'utf8' in your Sequel options to avoid encoding issues.
- run `bundle`
- run `bundle exec rackup -p 8088`
- open [http://localhost:8088](http://localhost:8088)