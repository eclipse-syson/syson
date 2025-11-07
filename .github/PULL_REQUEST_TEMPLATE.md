# PLEASE READ ALL ITEMS AND CHECK ONLY RELEVANT CHECKBOXES BELOW

## Auto review

- [ ] Have you reviewed this PR? Please do a first quick review, It is very useful to detect typos and missing copyrights, check comments, check your code... The reviewer will thank you for that :)

## Project management

- [ ] Has the pull request been added to the relevant milestone?
- [ ] Have the `priority:` and `pr:` labels been added to the pull request? (In case of doubt, start with the labels `priority: low` and `pr: to review later`)
- [ ] Have the relevant issues been added to the pull request?
- [ ] Have the relevant labels been added to the issues? (`area:`, `type:`)
- [ ] Have the relevant issues been added to the same project milestone as the pull request?

## Changelog and release notes

- [ ] Has the `CHANGELOG.adoc` + `doc/content/modules/user-manual/pages/release-notes/YYYY.MM.0.adoc` been updated to reference the relevant issues?
- [ ] Have the relevant API breaks been described in the `CHANGELOG.adoc`?
- [ ] Are the new / upgraded dependencies mentioned in the relevant section of the `CHANGELOG.adoc`?
- [ ] In case of a change with a visual impact, are there any screenshots in the `doc/content/modules/user-manual/pages/release-notes/YYYY.MM.0.adoc`?
- [ ] In case of a key change, has the change been added to `Key highlights` section in `doc/content/modules/user-manual/pages/release-notes/YYYY.MM.0.adoc`?

## Documentation

- [ ] Have you included an update of the [documentation](https://doc.mbse-syson.org) in your pull request? Please ask yourself if an update (installation manual, user manual, developer manual...) is needed and add one accordingly.

## Tests

- [ ] Is the code properly tested? Any pull request (fix, enhancement or new feature) should come with a test (or several). It could be unit tests, integration tests or cypress tests depending on the context. Only doc and releng pull request do not need for tests.
