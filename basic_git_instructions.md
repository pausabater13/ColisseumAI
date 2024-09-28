# Some basic intructions for introducing git use

## Cloning the Repository on Localhost

As of the current state of cryptography and based on recommendations from security experts, the **ED25519** algorithm is generally considered the most **secure** and **efficient** choice for SSH keys:

1. **Open a terminal** and create the SSH key in the `.ssh` folder with the key fingerprint:
    ```sh
    ssh-keygen -t ed25519
    ```
    It is *optional* to provide a name and password, but you can skip these by pressing **Enter**. This will create two files in the `.ssh` folder.

2. **Navigate to the SSH configuration folder**:
    ```sh
    cd ~/.ssh
    ```
    then display the content of the `.pub` file you created in the previous step and **copy the content**:
    ```sh
    cat id_ed25519.pub
    ```

3. In **GitHub** (*Profile > Settings > SSH and GPG keys*), create or add a new SSH key:
    - **Choose a name** for the key.
    - **Paste the content** of the `id_ed25519.pub` file as the key content.

4. In the terminal, while in the `.ssh` folder, **execute**:
    ```sh
    ssh-add id_ed25519
    ```

5. Finally, **navigate** to the directory where you want to clone the repository. Get, from GitHub, the SSH key associated with our repository, and then run:
    ```sh
    git clone git@github.com:pausabater13/Internship2024.git
    ```

# Essential Git Commands and How to Use Them
## `git status`
The `git status` command displays the state of the working directory and the staging area. It shows which changes have been staged, which haven’t, and which files aren’t being tracked by Git.

## `git init`
The `git init` command is used to initialize a new Git repository. This command sets up all the necessary files and directories for a Git repository in the current directory.

## `git clone`
The `git clone` command is used to create a copy of an existing repository. This command clones the entire repository, including its history.

### Usage:
```
git clone <repository-url>
```

## `git branch`
The `git branch` command is used to list, create, or delete branches. Branches are used to develop features, fix bugs, or experiment independently of the main codebase.

### List all branches:
```sh
git branch
```

### Create a new branch:
```sh
git branch <branch-name>
```

### Delete a branch
```sh
git branch -d <branch-name>
```

## `git checkout`
The `git checkout` is a command in Git that allows you to switch between different branches in your repository or restore files in your working directory to a previous state. Its functionality depends on how it's used:

### Switch your working (active) directory to the state of the specified branch:
```sh
git checkout <branch-name>
```
### Restore specific files in your working directory to a previous state:
```sh
git checkout -- <file_name>
```
### Create a new branch and switch to it in one step
```sh
git checkout -b new_branch_name
```
`git checkout` can discard uncommitted changes in your working directory, so it's a good idea to ensure you've committed your changes or stashed them before using this command. Additionally, in more recent versions of Git, `git switch` and `git restore` have been introduced as more intuitive alternatives for certain use cases, but `git checkout` remains widely used and understood.

## `git pull`
The `git pull` command fetches and merges changes from the remote repository into your current branch. This command is a combination of git fetch and git merge.

### Usage:
```sh
git pull <remote> <branch>
```

## `git add`
The `git add` command adds changes in the working directory to the staging area. This command tells Git that you want to include updates to a particular file in the next commit.

### Add a specific file:
```sh
git add <file>
```
### Add all changes:
```sh
git add .
```

## `git push`
The `git push` command is used to upload local repository content to a remote repository. This command sends your commits to the remote repository so others can access your changes.

### Usage:
```sh
git push <remote> <branch>
```

## `git commit`
The `git commit` command records changes to the repository. Each commit represents a snapshot of the repository at a specific point in time.

### Usage:
```sh
git commit -m "Commit message describing the changes"
```

## `git diff`
The `git diff` command shows the differences between files or commits. This command is useful for seeing what changes have been made before committing them.

## `git merge`
The `git merge` is a Git command used to integrate changes from one branch into another. It combines the changes made in a specified branch into the current branch. It can also be used to integrate changes from a remote branch into a local branch after fetching the latest changes from the remote repository.

### Usage:
To merge changes from one branch into another, you navigate to the branch where you want to incorporate the changes and then execute the merge command followed by the name of the branch you want to merge in.

```sh
git merge <branch_name>
```

### **Fast-Forward Merge**: 
If the current branch's commit is a direct descendant of the branch being merged, Git performs a **"fast-forward"** merge, meaning it simply moves the current branch pointer forward to the commit of the branch being merged. This happens when there are no new changes on the current branch since it diverged from the branch being merged.

### **Merge Commit**: 
If the branches being merged have diverged and have commits that are not present in each other, Git creates a new merge commit to tie together the histories of the merged branches. This merge commit has two parent commits, representing the states of the branches before the merge.

Sometimes, Git encounters conflicts during a merge when the changes in the branches being merged cannot be automatically reconciled. In such cases, Git halts the merge process and prompts the user to resolve the conflicts manually. Once conflicts are resolved, the changes can be staged, and the merge can be finalized by committing the merge result. It's essential to review the changes resulting from a merge to ensure that the integration was successful and to resolve any conflicts that may arise during the process. While `git merge` is a powerful tool for combining changes across branches, it's important to use it judiciously to maintain a clean and organized version history in your Git repository.

## `git log`
The `git log` command shows the commit history for the repository. This command is useful for viewing past changes and understanding the history of the project.


