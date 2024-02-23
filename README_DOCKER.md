# Build docker image

A docker image containing syson and its dependencies can be built using the [./Dockerfile](./Dockerfile) file.

* Copy the `.env.template` to `.env`
* Write your Github username, email and personal access token in `.env`
    * Note: you can generate a [Github personal access token here](https://github.com/settings/tokens). Select only the scope `read:packages`.
* Run the following:

```bash
docker build -t syson --secret id=tokens,src=.env .
```