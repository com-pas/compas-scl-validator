<!--
SPDX-FileCopyrightText: 2022 Alliander N.V.

SPDX-License-Identifier: Apache-2.0
-->

# Development

## Git

If the project is already cloned and a submodule is added use the following commands, first `git submodule init` and
next `git submodule update`.

More about Git Submodules can be found [here](https://git-scm.com/book/en/v2/Git-Tools-Submodules).

Tip: The URL to the submodules are configured in the file `.gitmodules`, but these are using the SSH URL. There is a way
described in the URL above that the URL can be overwritten locally with an HTTPS URL of the GIT Repository.

```
Since the URL in the .gitmodules file is what other people will first try to clone/fetch from, make sure to use a URL 
that they can access if possible. For example, if you use a different URL to push to than others would to pull from, 
use the one that others have access to. You can overwrite this value locally with git config 
submodule."riseclipse/riseclipse-developer".url HTTPS_URL for your own use. When applicable, a relative URL can be 
helpful.
```

## IntelliJ
