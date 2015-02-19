# met-api

The met-api is the implementation of a REST-based Application Programmer's
Interface (API) that can be used to search, browse and retrieve climate and
weather data.

The met-api is implemented in Scala using the
[Play Framework](https://playframework.com). The API is documented using
[Swagger](http://swagger.io).


## Licensing

Please see the LICENSE file.


## Installation

Clone the repository from Github:

`git clone https://github.com/metno/met-api.git`


### Dependencies

Install the Java 7 JDK and Unzip (required for packaging/deployment). On
Ubuntu, this can be done using apt-get:

`apt-get install  openjdk-7-jdk unzip`

Download and install the Play Framework (2.3.x at the time of writing).

[https://www.playframework.com/download](https://www.playframework.com/download)

Make sure to [install activator](https://www.playframework.com/documentation/2.3.x/Installing).


### Building

In your working directory, run:

`activator clean stage dist`

This should build the API and download any Play dependencies you may be
missing. It will also create a zip archive of the met-api under
`target/universal/met-api-#.#-SNAPSHOT.zip` that can be copied to a
production machine and deployed.

Unzip the distribution file at your preferred location, and run `bin/met-api`
to start the application. You can test the API by running
`wget http://hostname/v0/helloWorld`.


### Testing

To run the met-api in developer mode with swagger-ui on localhost, start it
with:

`activator run -Dswagger.api.basepath="http://localhost:9000"`

To run unit tests: `activator clean test`

To measure code coverage of the tests; `activator clean coverage test` and
to create the reports, `activator coverageReport`

To do basic style checking (without arcanist): `activator scalastyle`


## Documentation

The met-api is documented using Swagger UI. On a working instance of the API,
go to `http://hostname/v0/swagger-ui` or in a machine-readable form,
`http://hostname/v0/api-docs`

Developer documentation for the project can be found on MET Norway's developer
platform: [https://phab.met.no/tag/met_api/](https://phab.met.no/tag/met_api/).


## Contributing

We welcome contributions, and will help you get involved, if you're
interested.


### Comment on Issues

If you find a feature request particularly exciting or important, or you're
experiencing a particular bug, the best way to be heard by the #met_api team
is to comment on the task.

The list of current issues for the MET API are [here](https://phab.met.no/maniphest/?statuses=open&allProjects=PHID-PROJ-cf5xpz6j3jijthgt2lpr#R).


### Submit a Task(bug report/feature request)

Obviously, submitting a bug report is very helpful for us if you have
encountered a problem. Please [review the list of current issues](https://phab.met.no/maniphest/?statuses=open&allProjects=PHID-PROJ-cf5xpz6j3jijthgt2lpr#R)
first, however, to see if a similar or the same bug has already been reported.

You can also suggest to us what we can do to improve the MET API in future by
submitting a feature request. As above, please check that a similar feature
request has not already been submitted by another user. When submitting a
feature request, please be as descriptive as possible when creating the task,
and ideally describe a few use cases to show how useful this feature would be.

Issues are [submitted as Tasks using Maniphest](https://phab.met.no/maniphest/task/create/).
A tasks should contain enough information to be understandable and should be
tagged with the relevant Projects (you must add #met_api, otherwise we won't
see the task).


### Submitting patches

If you can code and want to directly help with the development of the MET API,
you are very welcome to contribute code. The following is important, if you
would like to contribute code:

- File a task with a bug report or feature request before you write code. This
will allows us to help you get started on the right foot, and we are much more
likely to accept a patch if we have discussed it first.
- Follow the [coding conventions of the project](https://phab.met.no/w/methodologies/development_workflow/#coding-conventions)
and try to submit your patch through arcanist or diffusion. Submitting through
our standard code review tools saves us a lot of work and significantly speeds
up our ability to respond to submitted patches.

The following documents may be helpful in setting up your development
environment:

- [MET API Developer Handbook](https://phab.met.no/w/methodologies/developer_handbook/)
- [Development Environment for Scala](https://phab.met.no/w/methodologies/developer_handbook/scala/)