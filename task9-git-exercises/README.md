# Git Exercises - Task 9

## Overview

Version control is an indispensable foundation of modern software engineering. The objective of **Task 9 - Git Exercises** is to develop a deep, production-grade proficiency with Git, ranging from day-to-day branching and conflict resolution workflows to advanced repository manipulation, history rewriting, bisecting regressions, and automating quality gates via Git hooks.

This module is divided into three distinct parts:
- **Part 1: Basic Git Operations (40 Points)**: Core repository configuration, aliases, branching models, feature development, merge conflict handling, and tagging.
- **Part 2: Advanced Git Operations (60 Points)**: Git stashing workflows, multi-commit interactive rebase (`squash`, `fixup`, `reword`), commit amending, reset strategies (`--soft`, `--mixed`, `--hard`), non-destructive reverts, rebase conflict resolution, reflog disaster recovery, repository sanitization with `git clean`, advanced diffing, regression tracking with `git bisect`, linked worktrees, custom log formatting, and patch management.
- **Part 3: Optional Advanced Operations**: Surgical commit extraction with `git cherry-pick`, line-by-line audit investigation with `git blame`, and automated client-side quality validation using custom `pre-commit`, `commit-msg`, and `pre-push` Git hooks.

Accompanying this guide is [git-commands-report.md](file:///Users/ujjwalkumar/Developer/java_tasks/task9-git-exercises/git-commands-report.md), which provides a student-style, step-by-step terminal execution log with all commands, explanations, and outputs.

---

## Part 1: Basic Git Operations (40 Points)

Part 1 simulates standard day-to-day team collaboration using a sample web project centered around [index.html](file:///Users/ujjwalkumar/Developer/java_tasks/task9-git-exercises/index.html).

### Step 1: Configure Git Identity and Understand Configuration Hierarchy
- **Purpose**: Configure Git user identity and understand configuration scope precedence (`system` < `global` < `local`).
- **Commands**:
  ```bash
  git config --system user.name "System Admin"
  git config --global user.name "Your Name"
  git config --global user.email "your.email@example.com"
  git config --local user.name "Repo Developer"
  git config --local user.email "developer@project.local"
  git config --list --show-origin
  ```
- **Expected Outcome**: Git verifies three tiers of configuration files (`/etc/gitconfig`, `~/.gitconfig`, and `.git/config`). The local repository-level setting takes highest priority and overrides both global and system levels.

### Step 2: Configure Essential Git Aliases
- **Purpose**: Establish shell productivity shortcuts for frequently executed Git commands.
- **Commands**:
  ```bash
  git config --global alias.co checkout
  git config --global alias.br branch
  git config --global alias.ci commit
  git config --global alias.st status
  git config --global alias.unstage "reset HEAD --"
  git config --global alias.last "log -1 HEAD"
  git config --global alias.lg "log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit"
  ```
- **Expected Outcome**: Running shorthand commands such as `git st` or `git lg` executes the full underlying command suite with custom graphical formatting.

### Step 3: Initialize Repository
- **Purpose**: Create an empty local Git repository.
- **Commands**:
  ```bash
  mkdir -p git-demo-repo
  cd git-demo-repo
  git init
  ```
- **Expected Outcome**: Git initializes an empty repository, creating the hidden `.git/` metadata directory and establishing the default primary branch (`main` or `master`).

### Step 4: Create and Switch to `develop` Branch
- **Purpose**: Establish an active development branch separate from production/main.
- **Commands**:
  ```bash
  git checkout -b develop
  # or in Git 2.23+:
  # git switch -c develop
  ```
- **Expected Outcome**: A new branch `develop` is created and becomes the currently active branch (`HEAD -> develop`).

### Step 5: Create Initial `index.html` File & Commit
- **Purpose**: Seed the repository with baseline project assets.
- **Commands**:
  ```bash
  # Ensure index.html exists with baseline content
  git status
  git add index.html
  git commit -m "feat: initial commit with basic HTML template"
  ```
- **Expected Outcome**: Working directory is clean; first baseline commit is recorded on `develop`.

### Step 6: Create First Feature Branch (`feature/header`)
- **Purpose**: Isolate new feature work on a dedicated branch branching off `develop`.
- **Commands**:
  ```bash
  git checkout -b feature/header
  ```
- **Expected Outcome**: Active branch switches to `feature/header` pointing to the commit created in Step 5.

### Step 7: Modify `index.html` with Header Navigation & Commit
- **Purpose**: Implement header navigation in `index.html`.
- **Commands**:
  ```bash
  # Edit index.html to add <header><nav>...</nav></header> inside <body>
  git diff
  git add index.html
  git commit -m "feat: add site navigation header to index.html"
  ```
- **Expected Outcome**: Commit recorded exclusively on `feature/header`; `develop` remains untouched.

### Step 8: Switch Back to `develop` Branch
- **Purpose**: Return to the shared integration branch to prepare for second feature.
- **Commands**:
  ```bash
  git checkout develop
  ```
- **Expected Outcome**: Working tree updates to `develop` state. The header modifications from `feature/header` are safely preserved on that branch and disappear from working tree.

### Step 9: Create Second Feature Branch (`feature/footer`)
- **Purpose**: Branch out a concurrent feature line off the original `develop` baseline.
- **Commands**:
  ```bash
  git checkout -b feature/footer
  ```
- **Expected Outcome**: `feature/footer` created branching from the same parent commit as `feature/header`.

### Step 10: Modify `index.html` with Footer & Commit
- **Purpose**: Implement page footer, intentionally modifying surrounding body lines to set up merge conflict.
- **Commands**:
  ```bash
  # Edit index.html to add <footer>...</footer> and alter the main body wrapper
  git diff
  git add index.html
  git commit -m "feat: add footer section to index.html"
  ```
- **Expected Outcome**: New commit created on `feature/footer`, creating diverging commit history between `feature/header` and `feature/footer`.

### Step 11: Switch to `develop` and Merge Feature Branch 1
- **Purpose**: Integrate `feature/header` into `develop`.
- **Commands**:
  ```bash
  git checkout develop
  git merge feature/header
  ```
- **Expected Outcome**: Fast-forward merge (or merge commit if `--no-ff` used); `develop` advances to include the header commit cleanly.

### Step 12: Merge Feature Branch 2 into `develop` (Triggering Conflict)
- **Purpose**: Trigger a concurrent content conflict between independent branches.
- **Commands**:
  ```bash
  git merge feature/footer
  ```
- **Expected Outcome**: Git halts automatic merging with output:
  `CONFLICT (content): Merge conflict in index.html`
  `Automatic merge failed; fix conflicts and then commit the result.`

### Step 13: Inspect and Resolve Merge Conflicts in `index.html`
- **Purpose**: Manually reconcile conflicting lines marked by Git conflict delimiters (`<<<<<<<`, `=======`, `>>>>>>>`).
- **Commands**:
  ```bash
  git status
  # Open index.html in editor, keep both <header> and <footer>, remove conflict markers
  git diff
  git add index.html
  git commit -m "merge: resolve merge conflict between feature/header and feature/footer"
  ```
- **Expected Outcome**: Conflict markers removed; both features combined cleanly; merge commit created.

### Step 14: Create Release Milestone Tag
- **Purpose**: Mark a stable milestone in project history using an annotated tag.
- **Commands**:
  ```bash
  git tag -a v1.0 -m "Release version 1.0 with header and footer"
  git tag -n
  git show v1.0
  ```
- **Expected Outcome**: Annotated tag `v1.0` created with author, timestamp, message, and cryptographic commit hash.

### Step 15: Merge `develop` into `main` / `master`
- **Purpose**: Promote tested code to the production branch.
- **Commands**:
  ```bash
  git checkout main 2>/dev/null || git checkout master
  git merge --no-ff develop -m "merge: release version 1.0 into production"
  ```
- **Expected Outcome**: Production branch updated via non-fast-forward merge commit, maintaining explicit audit history of the release.

### Step 16: Clean Up Feature Branches
- **Purpose**: Prune obsolete branches whose work has been integrated.
- **Commands**:
  ```bash
  git branch -d feature/header
  git branch -d feature/footer
  git branch -a
  ```
- **Expected Outcome**: Both local feature branches safely deleted.

### Step 17: Inspect and Verify Git History Graph
- **Purpose**: Validate complete branch divergence, resolution, and release points.
- **Commands**:
  ```bash
  git log --graph --oneline --decorate --all
  ```
- **Expected Outcome**: Visual ASCII tree displaying branch splits, merge junctions, tag `v1.0`, and `main`/`develop` pointers.

---

## Part 2: Advanced Git Operations (60 Points)

Part 2 focuses on Java engineering workflows centered around [Calculator.java](file:///Users/ujjwalkumar/Developer/java_tasks/task9-git-exercises/Calculator.java), simulating professional team scenarios including history cleaning, rebasing, disaster recovery, and bug localization.

### Step 1: Initialize Java Project & Commit Base `Calculator.java`
- **Purpose**: Seed repository with baseline Java class containing `add(int a, int b)`.
- **Commands**:
  ```bash
  git add Calculator.java
  git commit -m "feat: initial Calculator class with addition"
  ```
- **Explanation**: Creates the clean initial state from which arithmetic capabilities will evolve.

### Step 2: Git Stash Basics (`save`, `list`, `pop`, `apply`, `drop`)
- **Purpose**: Temporarily shelve uncommitted work to switch contexts without dirtying commit history.
- **Commands**:
  ```bash
  # Make changes to Calculator.java (e.g. begin subtract method)
  git status
  git stash save "WIP: subtraction method implementation"
  git status # Working directory clean
  git stash list
  git stash apply stash@{0}
  git stash drop stash@{0}
  # Or use single step: git stash pop
  ```
- **Explanation**: `git stash` saves uncommitted index and working tree changes onto an internal stack (`refs/stash`), allowing clean branch switching.

### Step 3: Stashing Untracked Files and Stash Branching
- **Purpose**: Preserve newly added files during stash operations and extract stash into a standalone branch.
- **Commands**:
  ```bash
  # Create untracked file CalculatorTest.java
  echo "class CalculatorTest {}" > CalculatorTest.java
  git stash -u -m "WIP: test harness and arithmetic updates"
  git stash list
  git stash branch feature/stash-work stash@{0}
  ```
- **Explanation**: The `-u` (`--include-untracked`) flag includes untracked files. `git stash branch` creates a new branch starting at the commit where the stash was created, applies the stash, and drops it automatically upon clean application.

### Step 4: Multi-commit Feature Development on `feature/calculator-ops`
- **Purpose**: Create multiple granular commits to simulate realistic active development.
- **Commands**:
  ```bash
  git checkout -b feature/calculator-ops
  # 1. Add subtract method -> commit
  git commit -am "feat: implement subtract"
  # 2. Add multiply method -> commit
  git commit -am "feat: implement multiply"
  # 3. Add divide method -> commit
  git commit -am "feat: implement divide"
  # 4. Add divide by zero check -> commit
  git commit -am "fix: add zero divisor validation to divide"
  ```
- **Explanation**: Builds a commit chain that will subsequently be organized and cleaned prior to merging.

### Step 5: Interactive Rebase (`squash`, `fixup`, `reword`)
- **Purpose**: Combine ("squash") related commits and reword commit messages to produce clean, atomic history.
- **Commands**:
  ```bash
  git rebase -i HEAD~4
  # In editor:
  # pick abc1234 feat: implement subtract
  # pick def2345 feat: implement multiply
  # pick ghi3456 feat: implement divide
  # fixup jkl4567 fix: add zero divisor validation to divide
  # (Save and close)
  git log --oneline -3
  ```
- **Explanation**: Interactive rebase rewrites local branch history. `squash` (or `s`) merges the commit into previous and prompts for a combined message; `fixup` (or `f`) discards the commit log in favor of the previous commit's message.

### Step 6: Amending the Last Commit (`git commit --amend`)
- **Purpose**: Incorporate missed edits or fix documentation without creating redundant "fix typo" commits.
- **Commands**:
  ```bash
  # Add Javadoc comments to Calculator.java
  git add Calculator.java
  git commit --amend -m "feat: implement divide with zero-check validation and javadocs"
  ```
- **Explanation**: Replaces tip of the current branch with a new commit containing the staged additions and updated message.

### Step 7: Deep Dive into Git Reset (`--soft`, `--mixed`, `--hard`)
- **Purpose**: Master the three reset modes and their impact on `HEAD`, `Index` (staging), and `Working Tree`.
- **Commands**:
  ```bash
  # Make temporary commit
  git commit -am "chore: temporary experimental change"
  
  # 1. Soft Reset (moves HEAD; keeps changes in index/staged)
  git reset --soft HEAD~1
  git status # Changes staged for commit
  
  # 2. Mixed Reset (default: moves HEAD; unstages changes into working directory)
  git add .
  git commit -m "chore: temporary experimental change"
  git reset --mixed HEAD~1
  git status # Changes not staged for commit
  
  # 3. Hard Reset (moves HEAD; wipes index AND working directory changes)
  git add .
  git commit -m "chore: temporary experimental change"
  git reset --hard HEAD~1
  git status # Clean working tree
  ```
- **Explanation**:
  - `--soft`: Changes HEAD reference only. Index and working tree unchanged.
  - `--mixed`: Changes HEAD reference and resets Index. Working tree preserves file edits.
  - `--hard`: Changes HEAD reference, resets Index, and resets working tree to match commit.

### Step 8: Non-destructive Reversals with `git revert`
- **Purpose**: Safely undo changes already published to shared/remote branches without rewriting history.
- **Commands**:
  ```bash
  # Add a buggy/deprecated method
  git commit -am "feat: add experimental modulo method"
  # Revert the commit cleanly:
  git revert HEAD --no-edit
  ```
- **Explanation**: `git revert` computes the inverse diff of the target commit and applies it as a brand-new commit, maintaining linear historical integrity.

### Step 9: Rebase vs. Merge for Upstream Synchronization
- **Purpose**: Synchronize feature branch with upstream updates without creating cluttering merge commits.
- **Commands**:
  ```bash
  # Switch to main and add upstream changes (e.g., pom.xml or documentation update)
  git checkout main
  git commit -am "docs: update project description in main"
  
  # Rebase feature branch onto latest main
  git checkout feature/calculator-ops
  git rebase main
  ```
- **Explanation**: Replay each feature commit on top of the newest `main` commit, resulting in a strictly linear history.

### Step 10: Handling and Resolving Rebase Conflicts
- **Purpose**: Successfully resolve conflicts that pause an interactive or standard rebase.
- **Commands**:
  ```bash
  # When rebase stops due to conflict:
  git status
  # Manually edit Calculator.java to resolve conflict
  git add Calculator.java
  git rebase --continue
  # In case of unresolvable error: git rebase --abort
  ```
- **Explanation**: Unlike merge conflicts (which resolve with a merge commit), rebase conflict resolution applies the fix directly to the replayed commit.

### Step 11: Emergency History Recovery with `git reflog`
- **Purpose**: Recover "lost" commits or accidentally deleted branches.
- **Commands**:
  ```bash
  # Simulate accidental hard reset
  git reset --hard HEAD~2
  git log --oneline # Commits appear lost
  
  # Check reflog for recent HEAD movements
  git reflog
  # Identify commit hash prior to reset (e.g., HEAD@{1})
  git reset --hard HEAD@{1}
  # Or restore into new branch:
  # git branch recovered-work HEAD@{1}
  ```
- **Explanation**: `git reflog` records every update to local reference tips (`HEAD`, branches). Unreferenced commits remain in Git's object database until pruned by garbage collection (~30–90 days).

### Step 12: Working Directory Cleanup with `git clean`
- **Purpose**: Remove untracked files and build artifacts without disturbing tracked code.
- **Commands**:
  ```bash
  # Generate temporary untracked files
  touch Calculator.class temp.log
  
  # Dry run (shows what would be removed)
  git clean -nd
  
  # Force deletion of untracked files and directories
  git clean -fd
  ```
- **Explanation**: `-n` performs a safety dry run; `-f` executes forced removal; `-d` includes untracked directories.

### Step 13: Advanced Git Diffing Strategies
- **Purpose**: Compare variations across working tree, index, commits, and branch forks.
- **Commands**:
  ```bash
  # Compare working tree vs staged index
  git diff
  # Compare staged index vs HEAD
  git diff --staged
  # Compare across commit ranges
  git diff HEAD~2 HEAD
  # Two-dot diff (difference between branch tips)
  git diff main..feature/calculator-ops
  # Three-dot diff (difference from common merge base)
  git diff main...feature/calculator-ops
  # File-scoped diff
  git diff main feature/calculator-ops -- Calculator.java
  ```
- **Explanation**: Understand index boundaries and compare branches relative to their common ancestor.

### Step 14: Bug Localization with `git bisect`
- **Purpose**: Pinpoint the exact commit that introduced a defect using binary search.
- **Commands**:
  ```bash
  # Start bisect session
  git bisect start
  # Mark current revision as bad
  git bisect bad
  # Mark known older working commit as good
  git bisect good <known-good-commit-sha>
  
  # Git checks out midpoint commit automatically:
  # Test code (javac Calculator.java && run tests)
  # Mark result:
  git bisect good # or git bisect bad
  # Repeat until Git identifies: "<sha> is the first bad commit"
  
  # End bisect and return to original HEAD:
  git bisect reset
  ```
- **Explanation**: Reduces commit inspection complexity from $O(N)$ linear scans to $O(\log N)$ binary search steps.

### Step 15: Parallel Workflows with Git Worktrees (`git worktree`)
- **Purpose**: Check out and work on multiple branches simultaneously without branch switching or separate repository clones.
- **Commands**:
  ```bash
  # Create a parallel worktree linked to branch hotfix
  git worktree add ../calculator-hotfix main
  
  # Inspect active worktrees
  git worktree list
  
  # Switch to worktree directory, execute hotfix, and commit
  cd ../calculator-hotfix
  # ... make fix and commit ...
  
  # Return and remove worktree
  cd -
  git worktree remove ../calculator-hotfix
  ```
- **Explanation**: Worktrees share the same `.git` object database while maintaining independent working directories and `HEAD` pointers.

### Step 16: Custom Log Formatting and Audit Filtering
- **Purpose**: Extract targeted audit trails and find commits introducing specific code tokens.
- **Commands**:
  ```bash
  # Custom formatted log graph
  git log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset'
  
  # Pickaxe search (find commits where string "divide" was added or removed)
  git log -S "divide" -p
  
  # Filter by author and date range
  git log --author="Developer" --since="2 weeks ago" --until="today"
  
  # Follow file across renames
  git log --follow -- Calculator.java
  ```
- **Explanation**: Enables rapid auditing, changelog generation, and historical forensics.

### Step 17: Generating and Applying Git Patches
- **Purpose**: Distribute and apply commit changes offline or via email/review systems.
- **Commands**:
  ```bash
  # Generate formatted email-ready patch files for last 2 commits
  mkdir -p patches
  git format-patch -2 HEAD -o patches/
  
  # Check if patch can be applied cleanly
  git apply --check patches/0001-*.patch
  
  # Apply patch with full commit metadata and author preserved
  git am patches/0001-*.patch
  
  # Generate raw diff patch and apply to working tree
  git diff HEAD~1 > custom-feature.patch
  git apply custom-feature.patch
  ```
- **Explanation**: `git format-patch` + `git am` preserves author, date, and commit message; `git diff` + `git apply` applies raw unstaged changes.

### Step 18: Release Tagging & Promotion
- **Purpose**: Seal the completed calculator module with an annotated release tag.
- **Commands**:
  ```bash
  git checkout main
  git merge --no-ff feature/calculator-ops -m "merge: integrate advanced calculator operations into main"
  git tag -a v2.0 -m "Release 2.0: Full Arithmetic Calculator with Robust Error Handling"
  git show v2.0
  ```
- **Expected Outcome**: `v2.0` permanently marks the completed arithmetic suite in the repository.

---

## Part 3: Optional Advanced Operations

### 1. `git cherry-pick`
- **Concept**: `git cherry-pick` enables selecting individual commits from an arbitrary branch and applying them onto the current working `HEAD` without performing a full branch merge.
- **Practical Use Cases**:
  - Porting an urgent bug fix from `main` or an unreleased feature branch directly to a maintenance/release branch (`v1.x`).
  - Salvaging useful commits from an abandoned experimental feature branch.
- **Key Commands**:
  ```bash
  # Cherry pick a single commit
  git cherry-pick <commit-sha>
  
  # Cherry pick and append origin commit info (-x)
  git cherry-pick -x <commit-sha>
  
  # Cherry pick without auto-committing (apply changes to staging)
  git cherry-pick -n <commit-sha>
  
  # Cherry pick a sequence of commits (from A exclusive to B inclusive)
  git cherry-pick <commit-A>..<commit-B>
  
  # Handling conflicts during cherry-pick:
  git status
  # ... resolve conflict ...
  git add <resolved-file>
  git cherry-pick --continue
  # Or abort:
  git cherry-pick --abort
  ```

### 2. `git blame`
- **Concept**: `git blame` annotates each line of a target file with the commit hash, author name, and timestamp of the last revision that modified that specific line.
- **Practical Use Cases**:
  - Investigating why a specific condition or algorithm was implemented.
  - Identifying the code author to consult regarding system behavior.
  - Verifying if recent commits altered critical lines.
- **Key Commands & Flags**:
  ```bash
  # Standard blame
  git blame Calculator.java
  
  # Blame specific line range (-L start,end)
  git blame -L 5,15 Calculator.java
  
  # Ignore whitespace changes (-w)
  git blame -w Calculator.java
  
  # Show author email instead of name (-e)
  git blame -e Calculator.java
  
  # Detect lines moved or copied from other files in same commit (-C)
  git blame -C Calculator.java
  
  # Detect lines moved or copied within the file (-M)
  git blame -M Calculator.java
  ```

### 3. Git Hooks
Git hooks are custom executable scripts residing in `.git/hooks/` that Git automatically runs before or after specific lifecycle actions (committing, pushing, rebasing). They enable client-side quality gates, code validation, and branch protection.

#### Hook Setup Instructions
1. Navigate to `.git/hooks/`.
2. Create or edit the hook file without an extension (e.g. `pre-commit`, `commit-msg`, `pre-push`).
3. Make the script executable:
   ```bash
   chmod +x .git/hooks/pre-commit
   chmod +x .git/hooks/commit-msg
   chmod +x .git/hooks/pre-push
   ```

---

## Git Hooks Examples

### 1. `pre-commit` Hook: Java Code Validation & Conflict Marker Prevention
Prevents commits if code contains unresolved merge conflict markers, raw `System.out.println`, or fails Java compilation.

```bash
#!/usr/bin/env bash
# .git/hooks/pre-commit
# Enforces code hygiene and compilation checks before committing

echo "==> Running pre-commit validation checks..."

# Check 1: Reject unresolved merge conflict markers
if git diff --cached | grep -E '^(<{7}|={7}|>{7})'; then
    echo "ERROR: Commit rejected! Unresolved merge conflict markers found."
    exit 1
fi

# Check 2: Reject debug stdout in Java sources
STAGED_JAVA_FILES=$(git diff --cached --name-only --diff-filter=ACM | grep '\.java$')
if [ -n "$STAGED_JAVA_FILES" ]; then
    for file in $STAGED_JAVA_FILES; do
        if grep -n 'System\.out\.println' "$file" > /dev/null 2>&1; then
            echo "WARNING: Raw System.out.println detected in $file."
            echo "Consider using a logger instead."
        fi
    done
    
    # Check 3: Validate syntax compilation of staged Java files
    echo "Compiling staged Java files to verify syntax..."
    javac -d /tmp $STAGED_JAVA_FILES
    if [ $? -ne 0 ]; then
        echo "ERROR: Java compilation failed on staged files! Commit aborted."
        exit 1
    fi
fi

echo "==> Pre-commit checks passed successfully!"
exit 0
```

### 2. `commit-msg` Hook: Conventional Commit Format Enforcement
Validates that all commit messages follow team standard prefixes (`feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `chore:`) or include a Jira issue ticket key (`[JMP-XXX]`).

```bash
#!/usr/bin/env bash
# .git/hooks/commit-msg
# Enforces conventional commit messages or Jira issue ticket format

COMMIT_MSG_FILE=$1
COMMIT_MSG=$(head -n 1 "$COMMIT_MSG_FILE")

# Pattern: Conventional commit regex OR [JMP-123] prefix
PATTERN="^((feat|fix|docs|style|refactor|perf|test|chore|merge)(\([a-z0-9_-]+\))?: .+|\[(JMP|TASK)-[0-9]+\]: .+)"

if ! [[ "$COMMIT_MSG" =~ $PATTERN ]]; then
    echo "================================================================="
    echo "ERROR: Invalid commit message format!"
    echo "-----------------------------------------------------------------"
    echo "Your commit message: \"$COMMIT_MSG\""
    echo ""
    echo "Accepted formats:"
    echo "  1. Conventional Commits:  feat: implement divide method"
    echo "                            fix(core): handle divide by zero"
    echo "                            docs: update README with hooks"
    echo "  2. Jira Ticket Prefix:    [JMP-101]: add Calculator class"
    echo "================================================================="
    exit 1
fi

exit 0
```

### 3. `pre-push` Hook: Prevent Direct Pushes to Protected Branches & Run Tests
Prevents pushing broken code or pushing directly to `main` without a Pull Request.

```bash
#!/usr/bin/env bash
# .git/hooks/pre-push
# Protects production branches and enforces automated tests before pushing

CURRENT_BRANCH=$(git symbolic-ref --short HEAD)

# Rule 1: Prevent direct push to main or production
if [ "$CURRENT_BRANCH" = "main" ] || [ "$CURRENT_BRANCH" = "master" ]; then
    echo "ERROR: Direct push to '$CURRENT_BRANCH' is strictly forbidden!"
    echo "Please push your feature branch and create a Pull Request / Merge Request."
    exit 1
fi

# Rule 2: Execute build and test verification if Maven is present
if [ -f "pom.xml" ]; then
    echo "==> Running Maven validation tests prior to push..."
    mvn clean test -q
    if [ $? -ne 0 ]; then
        echo "ERROR: Maven tests failed! Push rejected."
        exit 1
    fi
    echo "==> Maven tests passed!"
fi

exit 0
```

---

## Report: Required Evidence and Screenshots Checklist

When submitting the Git task report, capture terminal screenshots or formatted log outputs for each of the following milestones:

### Part 1 Evidence Checklist
1. **Git Configuration**: Screenshot of `git config --list --show-origin` proving system, global, and local scope precedence.
2. **Git Aliases**: Output of `git config --get-regexp alias` demonstrating configured shortcuts.
3. **Branch Setup**: Output of `git branch -v` after creating `develop`.
4. **Feature Branches**: Output of `git branch -a` showing `develop`, `feature/header`, and `feature/footer`.
5. **Merge Conflict Trigger**: Terminal screenshot of `git merge feature/footer` showing `CONFLICT (content): Merge conflict in index.html`.
6. **Conflict Resolution**: Screenshot of `index.html` displaying resolved header and footer code without conflict markers.
7. **Release Tag**: Terminal output of `git show v1.0` verifying tag metadata and annotated message.
8. **Final Part 1 Log Graph**: Output of `git log --graph --oneline --decorate --all` displaying the completed branch-and-merge structure.

### Part 2 Evidence Checklist
1. **Stash Operations**: Output of `git stash list` showing saved stash entries and `git stash pop` restoration.
2. **Stash Untracked & Branch**: Output showing untracked file stashed with `-u` and restored via `git stash branch`.
3. **Interactive Rebase**: Screenshot of the interactive rebase editor window showing `pick`, `squash`, or `fixup` directives.
4. **Cleaned Commit Log**: Output of `git log --oneline -5` showing consolidated commits.
5. **Amend Operation**: Output of `git show HEAD` proving the last commit was updated with Javadocs without an extra commit node.
6. **Reset Comparison**: Terminal outputs comparing `git reset --soft`, `--mixed`, and `--hard`.
7. **Git Revert**: Output of `git log -2` showing the original commit followed by the revert commit.
8. **Rebase Synchronization**: Output of `git log --graph --oneline` showing feature branch rebased linearly on top of `main`.
9. **Rebase Conflict Resolution**: Screenshot of rebase conflict pause and `git rebase --continue`.
10. **Reflog Disaster Recovery**: Screenshot of `git reflog` showing recovered commit hash after an accidental `git reset --hard`.
11. **Directory Cleanup**: Output of `git clean -nd` dry run followed by `git clean -fd`.
12. **Advanced Diff**: Screenshots of `git diff`, `git diff --staged`, and `git diff main...feature/calculator-ops`.
13. **Bisect Localization**: Complete terminal log of `git bisect` identifying the first bad commit.
14. **Worktree Isolation**: Output of `git worktree list` displaying simultaneous working directories.
15. **Custom Log Filters**: Output of `git log -S "divide" -p` showing exact commits introducing the method.
16. **Patch Creation and Application**: Screenshot of generated `0001-*.patch` and successful `git am` execution.
17. **Final Part 2 Tag**: Terminal output of `git show v2.0`.

### Part 3 Evidence Checklist
1. **Cherry-Pick**: Screenshot showing `git cherry-pick -x <sha>` applying commit onto target branch.
2. **Git Blame**: Output of `git blame -L 1,15 Calculator.java` displaying author attribution per line.
3. **Pre-commit Hook**: Screenshot showing pre-commit hook rejecting a commit with conflict markers and allowing clean commit.
4. **Commit-msg Hook**: Screenshot showing hook blocking invalid message and accepting `feat: ...` format.
5. **Pre-push Hook**: Screenshot of hook blocking direct push to `main`.
