# FaceBroke

A deliberately hackable social network app


### Requirements

- Docker
- docker-compose

The best way to run FaceBroke is within Docker. Before continuing, make sure that you have [Docker](https://www.docker.com/community-edition#/download)  installed and can run it from the command line.
If you're running Linux, you'll also need to manually install [docker-compose](https://docs.docker.com/compose/install/)

### Running

1. Clone this repo with: `git clone https://github.com/softwaresecured/FaceBroke.git`

2. Go to the cloned directory: `cd FaceBroke/`

3. run: `docker-compose up --build -d`

This will launch the FaceBroke project. You can view the web interface at http://127.0.0.1:8081 and the Postgresql server is exposed on port 5432 to the host environment.

### Rebuilding

1. While still in the project directory, run: `docker-compose up --build -d`

### Stopping

1. While still in the project directory, run: `docker-compose down`




# UNDER DEVELOPMENT

## Bug naming

Two branches of code exist here:

- _master_ --> for the working app.
- _vulnerable_ --> where intentional vulnerabilities have been introduced and tagged

In order to accurately track these 'bugs', we need a naming convention. Current idea is:

`{filename}-{previous commit hash}-{starting line of bug}`
