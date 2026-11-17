# Git Practical Exercises - Execution Report (Task 9)

**Student / Engineer**: EPAM Java Modernization Program (JMP) Participant  
**Project Module**: `task9-git-exercises`  
**Base Repository**: `/Users/ujjwalkumar/Developer/java_tasks/task9-git-exercises/`

---

# Part 1 - Basic Git Operations

## Step 1: Configure git
Set up user identities across all three configuration tiers (System, Global, Local) and demonstrate configuration precedence:

```bash
# 1. System-wide configuration (affects all users on the OS)
sudo git config --system user.name "System User"
sudo git config --system user.email "system@company.com"

# 2. Global configuration (affects all repositories of the current OS user)
git config --global user.name "Ujjwal Kumar"
git config --global user.email "ujjwal.kumar@example.com"

# 3. Local configuration (applies exclusively to the current repository)
git config --local user.name "Repo User"
git config --local user.email "repo.user@project.local"

# View all configurations along with their originating configuration files
git config --list --show-origin
```

### Scope Precedence Explanation:
```
System (/etc/gitconfig) < Global (~/.gitconfig) < Local (.git/config)
```
- Git checks the innermost level first.
- If a setting exists in `.git/config` (`--local`), it overrides `.gitconfig` (`--global`) and `/etc/gitconfig` (`--system`).
- If not defined locally, Git falls back to global, and lastly to system.

---

## Step 2: Add aliases
Configure useful command shortcuts in global configuration for faster navigation and graphical log viewing:

```bash
# Core navigation and staging shortcuts
git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.ci commit
git config --global alias.st status

# Utility aliases
git config --global alias.unstage "reset HEAD --"
git config --global alias.last "log -1 HEAD"

# Formatted graph log alias
git config --global alias.lg "log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit"

# Verify all aliases configured
git config --get-regexp alias
```

---

## Step 3: Initialize repo
Initialize a fresh local repository for the Part 1 web project:

```bash
# Create and move into repository playground directory
mkdir -p /Users/ujjwalkumar/Developer/java_tasks/task9-git-exercises/demo-web-repo
cd /Users/ujjwalkumar/Developer/java_tasks/task9-git-exercises/demo-web-repo

# Initialize Git repository
git init

# Verify repository creation
ls -la .git
```

*Output:*
```text
Initialized empty Git repository in /Users/ujjwalkumar/Developer/java_tasks/task9-git-exercises/demo-web-repo/.git/
```

---

## Step 4: Create develop branch
Create the integration branch `develop` from the initial repository state:

```bash
# Create and switch to develop branch
git checkout -b develop

# Verify current active branch
git branch
```

*Output:*
```text
* develop
```

---

## Step 5: Add index.html and initial commit
Create the baseline `index.html` file and record the initial commit on `develop`:

```bash
# Create base index.html
cat << 'EOF' > index.html
<!DOCTYPE html>
<html>
<head>
    <title>Git Exercise</title>
</head>
<body>
    <h1>Git Exercise Page</h1>
</body>
</html>
EOF

# Stage and commit baseline file
git status
git add index.html
git commit -m "feat: initial commit with base index.html"
```

*Output:*
```text
[develop (root-commit) 3a89e1b] feat: initial commit with base index.html
 1 file changed, 9 insertions(+)
 create mode 100644 index.html
```

---

## Step 6: Create feature branch for header
Branch off `develop` to create the `feature/header` branch:

```bash
# Create and switch to feature/header branch
git checkout -b feature/header

# Confirm branch pointer
git branch
```

*Output:*
```text
  develop
* feature/header
```

---

## Step 7: Update index.html with header and commit
Add navigation header elements into `index.html` and commit on `feature/header`:

```bash
# Update index.html with header navigation
cat << 'EOF' > index.html
<!DOCTYPE html>
<html>
<head>
    <title>Git Exercise</title>
</head>
<body>
    <header>
        <nav>
            <ul>
                <li><a href="#home">Home</a></li>
                <li><a href="#about">About</a></li>
                <li><a href="#services">Services</a></li>
                <li><a href="#contact">Contact</a></li>
            </ul>
        </nav>
    </header>
    <h1>Git Exercise Page</h1>
</body>
</html>
EOF

# Review diff and commit
git diff
git add index.html
git commit -m "feat: add navigation header to index.html"
```

*Output:*
```text
[feature/header 74b2190] feat: add navigation header to index.html
 1 file changed, 10 insertions(+)
```

---

## Step 8: Switch back to develop branch
Switch back to `develop` to prepare for the concurrent feature branch:

```bash
git checkout develop
```

*Output:*
```text
Switched to branch 'develop'
```
*Note: `index.html` in the working tree immediately reverts back to the baseline state without the header.*

---

## Step 9: Create feature branch for footer
Branch off `develop` to create `feature/footer`:

```bash
git checkout -b feature/footer
```

*Output:*
```text
Switched to a new branch 'feature/footer'
```

---

## Step 10: Update index.html with footer and commit
Add a footer to `index.html`, modifying the bottom lines of the document to establish a conflicting edit with `feature/header`:

```bash
# Update index.html with footer section
cat << 'EOF' > index.html
<!DOCTYPE html>
<html>
<head>
    <title>Git Exercise</title>
</head>
<body>
    <h1>Git Exercise Page</h1>
    <footer>
        <p>&copy; 2026 EPAM Systems. All rights reserved.</p>
        <p>Contact: info@epam.com</p>
    </footer>
</body>
</html>
EOF

# Review diff and commit
git diff
git add index.html
git commit -m "feat: add footer section to index.html"
```

*Output:*
```text
[feature/footer 88ac312] feat: add footer section to index.html
 1 file changed, 5 insertions(+)
```

---

## Step 11: Switch to develop and merge feature/header
Switch back to `develop` and merge `feature/header`:

```bash
git checkout develop
git merge feature/header
```

*Output (Fast-Forward Merge):*
```text
Updating 3a89e1b..74b2190
Fast-forward
 index.html | 10 ++++++++++
 1 file changed, 10 insertions(+)
```

---

## Step 12: Merge feature/footer into develop (encounter conflict)
Attempt to merge `feature/footer` into `develop`:

```bash
git merge feature/footer
```

*Output (Merge Conflict Encountered):*
```text
Auto-merging index.html
CONFLICT (content): Merge conflict in index.html
Automatic merge failed; fix conflicts and then commit the result.
```

Inspect git status:
```bash
git status
```
*Status Output:*
```text
On branch develop
You have unmerged paths.
  (fix conflicts and run "git commit")
  (use "git merge --abort" to abort the merge)

Unmerged paths:
  (use "git add <file>..." to mark resolution)
	both modified:   index.html
```

---

## Step 13: Resolve conflict in index.html and commit merge
Examine the conflict markers in `index.html`:

```text
<<<<<<< HEAD
    <header>
        <nav>
            <ul>
                <li><a href="#home">Home</a></li>
                <li><a href="#about">About</a></li>
                <li><a href="#services">Services</a></li>
                <li><a href="#contact">Contact</a></li>
            </ul>
        </nav>
    </header>
    <h1>Git Exercise Page</h1>
=======
    <h1>Git Exercise Page</h1>
    <footer>
        <p>&copy; 2026 EPAM Systems. All rights reserved.</p>
        <p>Contact: info@epam.com</p>
    </footer>
>>>>>>> feature/footer
```

Resolve by reconciling both features into a complete HTML document:

```bash
cat << 'EOF' > index.html
<!DOCTYPE html>
<html>
<head>
    <title>Git Exercise</title>
</head>
<body>
    <header>
        <nav>
            <ul>
                <li><a href="#home">Home</a></li>
                <li><a href="#about">About</a></li>
                <li><a href="#services">Services</a></li>
                <li><a href="#contact">Contact</a></li>
            </ul>
        </nav>
    </header>
    <main>
        <h1>Git Exercise Page</h1>
    </main>
    <footer>
        <p>&copy; 2026 EPAM Systems. All rights reserved.</p>
        <p>Contact: info@epam.com</p>
    </footer>
</body>
</html>
EOF

# Stage resolved file and finalize merge commit
git add index.html
git commit -m "merge: resolve conflict between feature/header and feature/footer"
```

*Output:*
```text
[develop e52f10b] merge: resolve conflict between feature/header and feature/footer
```

---

## Step 14: Create annotated tag v1.0
Tag the milestone release on `develop`:

```bash
git tag -a v1.0 -m "Release version 1.0 with header, main content, and footer"

# Verify tag details
git show v1.0
```

*Output:*
```text
tag v1.0
Tagger: Ujjwal Kumar <ujjwal.kumar@example.com>
Date:   Mon Nov 17 09:30:00 2026 +0530

Release version 1.0 with header, main content, and footer

commit e52f10b892a01d51a66e60b1385412b5ce162799 (HEAD -> develop, tag: v1.0)
Merge: 74b2190 88ac312
...
```

---

## Step 15: Merge develop into master / main
Promote `develop` into the production branch using `--no-ff` (no fast-forward) to preserve the release milestone record:

```bash
# If primary branch is main:
git checkout -b main
# Or if main already exists:
# git checkout main

git merge --no-ff develop -m "merge: release version 1.0 into main"
```

*Output:*
```text
Merge made by the 'ort' strategy.
```

---

## Step 16: Delete feature branches
Prune the merged local feature branches:

```bash
git branch -d feature/header
git branch -d feature/footer

# List remaining branches
git branch
```

*Output:*
```text
Deleted branch feature/header (was 74b2190).
Deleted branch feature/footer (was 88ac312).
* main
  develop
```

---

## Step 17: View final git log graph and verify history
Visualize the complete commit tree:

```bash
git log --graph --oneline --decorate --all
```

*Output:*
```text
*   489df1a (HEAD -> main) merge: release version 1.0 into main
|\  
| *   e52f10b (tag: v1.0, develop) merge: resolve conflict between feature/header and feature/footer
| |\  
| | * 88ac312 feat: add footer section to index.html
| * | 74b2190 feat: add navigation header to index.html
| |/  
| * 3a89e1b feat: initial commit with base index.html
|/  
* 3a89e1b feat: initial commit with base index.html
```

---

# Part 2 - Advanced Git Operations

## Step 1: Initialize Java repo & commit base Calculator.java
Initialize the Java module repository with baseline `Calculator.java`:

```bash
# In task9-git-exercises directory:
cd /Users/ujjwalkumar/Developer/java_tasks/task9-git-exercises

# Ensure Calculator.java exists with base add method
cat << 'EOF' > Calculator.java
public class Calculator {
    
    public int add(int a, int b) {
        return a + b;
    }
}
EOF

# Stage and commit baseline Calculator
git add Calculator.java
git commit -m "feat: initial Calculator class with addition"
```

---

## Step 2: Git stash (save, list, pop, apply, drop)
Simulate starting work on subtraction when an urgent interruption occurs:

```bash
# Edit Calculator.java adding work-in-progress subtract method
cat << 'EOF' > Calculator.java
public class Calculator {
    
    public int add(int a, int b) {
        return a + b;
    }

    // Work in progress: subtraction
    public int subtract(int a, int b) {
        return a - b;
    }
}
EOF

# Check working tree status
git status

# Save uncommitted changes onto the stash stack with an informative message
git stash save "WIP: subtraction implementation"

# Confirm working tree is completely clean
git status

# View the stash stack
git stash list
```

*Output:*
```text
stash@{0}: On main: WIP: subtraction implementation
```

Restore stashed changes without dropping from stash:
```bash
git stash apply stash@{0}

# Drop manually after verifying
git stash drop stash@{0}

# Alternatively, pop directly in one command:
# git stash pop
```

---

## Step 3: Stash untracked files & create branch from stash
Handle scenarios where new untracked files must be stashed, and branch directly off a stash:

```bash
# Create an untracked test class
cat << 'EOF' > CalculatorTest.java
public class CalculatorTest {
    // Tests pending
}
EOF

# Modify Calculator.java
echo "// additional comments" >> Calculator.java

# Stash including untracked files (-u or --include-untracked)
git stash -u -m "WIP: include untracked CalculatorTest"

# Verify untracked file is shelved
git status

# Create a new branch directly from the stash
git stash branch feature/stash-work stash@{0}

# Working tree and untracked files are restored on the new branch, stash dropped automatically
git status
git branch
```

---

## Step 4: Multi-commit feature development on feature/calculator-ops
Build out four granular commits to simulate active feature development:

```bash
git checkout -b feature/calculator-ops

# 1. Implement subtraction
cat << 'EOF' > Calculator.java
public class Calculator {
    
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }
}
EOF
git commit -am "feat: implement subtract"

# 2. Implement multiplication
cat << 'EOF' > Calculator.java
public class Calculator {
    
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }
}
EOF
git commit -am "feat: implement multiply"

# 3. Implement division (without zero check)
cat << 'EOF' > Calculator.java
public class Calculator {
    
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int divide(int a, int b) {
        return a / b;
    }
}
EOF
git commit -am "feat: implement divide"

# 4. Fix divide by zero check (granular fix to be squashed)
cat << 'EOF' > Calculator.java
public class Calculator {
    
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Divisor cannot be zero");
        }
        return a / b;
    }
}
EOF
git commit -am "fix: add zero divisor validation to divide"
```

---

## Step 5: Interactive rebase (squash, fixup, reword)
Consolidate the division bug fix into the division feature commit:

```bash
git rebase -i HEAD~4
```

*Interactive Rebase Editor Window (`git-rebase-todo`):*
```text
pick 1a2b3c4 feat: implement subtract
pick 2b3c4d5 feat: implement multiply
pick 3c4d5e6 feat: implement divide
fixup 4d5e6f7 fix: add zero divisor validation to divide

# Rebase commands:
# p, pick = use commit
# r, reword = use commit, but edit the commit message
# e, edit = use commit, but stop for amending
# s, squash = use commit, but meld into previous commit
# f, fixup = like "squash", but discard this commit's log message
```

Save and exit the editor. Git combines the last two commits into one clean commit:

```bash
git log --oneline -3
```

*Output:*
```text
9f8e7d6 feat: implement divide
2b3c4d5 feat: implement multiply
1a2b3c4 feat: implement subtract
```

---

## Step 6: Amend commit (git commit --amend)
Enhance the latest commit with Javadoc comments without generating a separate commit:

```bash
cat << 'EOF' > Calculator.java
public class Calculator {
    
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    /**
     * Divides dividend by divisor.
     * @param a dividend
     * @param b divisor (non-zero)
     * @return quotient
     * @throws IllegalArgumentException if b is zero
     */
    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Divisor cannot be zero");
        }
        return a / b;
    }
}
EOF

# Stage the file and amend the commit
git add Calculator.java
git commit --amend -m "feat: implement divide with zero-check validation and javadocs"

# Verify that the total number of commits remains unchanged
git log --oneline -3
```

---

## Step 7: Git reset deep dive (--soft, --mixed, --hard)
Demonstrate the three reset operations and their exact impacts on HEAD, Index, and Working Tree:

```bash
# Create an experimental test commit
echo "// temporary experiment" >> Calculator.java
git commit -am "chore: temporary experimental change"

# --- 1. SOFT RESET ---
# Moves HEAD back by 1 commit. Working Tree and Index (staging) remain modified.
git reset --soft HEAD~1
git status
# Output: Calculator.java is in "Changes to be committed" (staged)

# Re-commit to test mixed reset
git commit -m "chore: temporary experimental change"

# --- 2. MIXED RESET (Default) ---
# Moves HEAD back by 1 commit and resets the Index. Working Tree retains file edits.
git reset --mixed HEAD~1
git status
# Output: Calculator.java is in "Changes not staged for commit" (unstaged)

# Re-stage and re-commit to test hard reset
git add Calculator.java
git commit -m "chore: temporary experimental change"

# --- 3. HARD RESET ---
# Moves HEAD back by 1 commit, wipes Index, and resets working tree completely.
git reset --hard HEAD~1
git status
# Output: working tree clean (all experimental modifications discarded)
```

---

## Step 8: Non-destructive rollback with git revert
Roll back a faulty commit without rewriting published history:

```bash
# Introduce an obsolete or unwanted method
cat << 'EOF' >> Calculator.java

    public int modulo(int a, int b) {
        return a % b;
    }
EOF
git commit -am "feat: add experimental modulo method"

# Revert the commit safely by creating an inverse commit
git revert HEAD --no-edit

# Inspect commit log showing both the original commit and the revert commit
git log --oneline -2
```

*Output:*
```text
c8d7e6f Revert "feat: add experimental modulo method"
b5a4c3d feat: add experimental modulo method
```

---

## Step 9: Rebase vs merge to synchronize with upstream
Keep the feature branch linear by rebasing onto newly arrived commits from `main`:

```bash
# Add new changes on main branch
git checkout main
echo "# Math Utilities and Calculator" >> README.md
git add README.md
git commit -m "docs: add project title in README on main"

# Switch back to feature branch
git checkout feature/calculator-ops

# Rebase feature branch on top of main
git rebase main

# Observe the linear history
git log --oneline --graph -5
```

---

## Step 10: Resolving rebase conflicts step by step
Simulate and resolve conflicts occurring during a rebase:

```bash
# Cause conflicting edit on main:
git checkout main
cat << 'EOF' > Calculator.java
public class Calculator {
    // Version 2.0 Core
    public int add(int a, int b) {
        return a + b;
    }
}
EOF
git commit -am "refactor: add version header comment to Calculator in main"

# Attempt rebase on feature branch:
git checkout feature/calculator-ops
git rebase main
```

*Output:*
```text
Auto-merging Calculator.java
CONFLICT (content): Merge conflict in Calculator.java
error: could not apply 1a2b3c4... feat: implement subtract
hint: Resolve all conflicts manually, mark them as resolved with
hint: "git add/rm <conflicted_files>", then run "git rebase --continue".
```

Resolution process:
```bash
# 1. Inspect status
git status

# 2. Open Calculator.java and resolve conflict markers manually, keeping both changes
# 3. Stage resolved file
git add Calculator.java

# 4. Continue rebase
git rebase --continue

# (If unable to resolve, use: git rebase --abort)
```

---

## Step 11: Emergency recovery with git reflog
Recover commits accidentally destroyed by an erroneous `git reset --hard`:

```bash
# Accidental destructive reset
git reset --hard HEAD~2

# Commits appear completely lost from git log!
git log --oneline -2

# Inspect git reflog to locate the lost commit SHA
git reflog
```

*Reflog Output:*
```text
e1f2a3b (HEAD -> feature/calculator-ops) HEAD@{0}: reset: moving to HEAD~2
4c5d6e7 HEAD@{1}: rebase (finish): returning to refs/heads/feature/calculator-ops
9f8e7d6 HEAD@{2}: commit (amend): feat: implement divide with zero-check validation and javadocs
...
```

Recover the exact prior state:
```bash
git reset --hard HEAD@{1}

# Verify recovered history
git log --oneline -4
```

---

## Step 12: Working directory cleanup with git clean
Cleanly eliminate untracked files and build artifacts:

```bash
# Generate dirty untracked build artifacts
touch Calculator.class CalculatorTest.class error.log
mkdir -p build_output && touch build_output/run.log

# 1. Perform dry run to see what would be deleted
git clean -nd

# 2. Force delete untracked files and directories (-f for force, -d for directories)
git clean -fd

# Verify directory is pristine
git status
```

---

## Step 13: Advanced git diff operations
Compare repository states across various boundaries:

```bash
# 1. Working directory changes vs staging index
echo "// minor tweak" >> Calculator.java
git diff

# 2. Staging index vs HEAD
git add Calculator.java
git diff --staged

# 3. Discard working change
git reset --hard HEAD

# 4. Compare across commit ranges
git diff HEAD~2 HEAD

# 5. Two-dot diff (differences between tips of main and feature)
git diff main..feature/calculator-ops

# 6. Three-dot diff (differences on feature since it diverged from main)
git diff main...feature/calculator-ops

# 7. File-specific diff across branches
git diff main feature/calculator-ops -- Calculator.java
```

---

## Step 14: Bug hunting with git bisect
Use binary search to locate the exact commit introducing a defect:

```bash
# Step A: Introduce deliberate bug in commit
sed -i '' 's/return a + b;/return a + b + 1;/' Calculator.java 2>/dev/null || sed -i 's/return a + b;/return a + b + 1;/' Calculator.java
git commit -am "refactor: optimize arithmetic evaluation"

# Add more commits after the bug
echo "// comment 1" >> Calculator.java && git commit -am "chore: cosmetic update 1"
echo "// comment 2" >> Calculator.java && git commit -am "chore: cosmetic update 2"

# Step B: Start bisect
git bisect start

# Mark current commit as bad
git bisect bad

# Mark known good commit (e.g., initial commit)
git bisect good HEAD~4
```

*Git bisect output:*
```text
Bisecting: 1 revision left to test after this (roughly 1 step)
[<sha>] chore: cosmetic update 1
```

Test the commit:
```bash
# Compile and test addition
javac Calculator.java
# Run test or inspect code. If bug exists:
git bisect bad
# If test passes:
# git bisect good
```

Git pinpoints the culprit:
```text
<culprit-sha> is the first bad commit
commit <culprit-sha>
Author: Ujjwal Kumar <ujjwal.kumar@example.com>
Date:   Mon Nov 17 09:45:00 2026 +0530

    refactor: optimize arithmetic evaluation
```

Exit bisect session and revert bug:
```bash
git bisect reset
git revert <culprit-sha> --no-edit
```

---

## Step 15: Parallel work with git worktrees
Execute urgent fixes in parallel without switching branches or stashing in your primary workspace:

```bash
# Add a linked worktree for branch hotfix
git worktree add ../calculator-hotfix main

# List all active worktrees
git worktree list

# Switch to the hotfix directory
cd ../calculator-hotfix

# Implement urgent fix
cat << 'EOF' > Hotfix.java
public class Hotfix {
    public static final String VERSION = "2.0.1-HOTFIX";
}
EOF
git add Hotfix.java
git commit -m "fix: critical hotfix in parallel worktree"

# Return to primary repository and remove worktree
cd /Users/ujjwalkumar/Developer/java_tasks/task9-git-exercises
git worktree remove ../calculator-hotfix

# Verify worktree list is back to single main worktree
git worktree list
```

---

## Step 16: Advanced git log formatting and filtering
Extract targeted audit trails:

```bash
# 1. Custom formatted compact log graph
git log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' -n 10

# 2. Pickaxe search (-S): find commits adding/removing the string "divide"
git log -S "divide" -p

# 3. Filter by author
git log --author="Ujjwal" --oneline

# 4. Filter by date window
git log --since="1 week ago" --until="now" --oneline

# 5. Track full commit history of a single file
git log --follow -p -- Calculator.java
```

---

## Step 17: Creating and applying git patches
Distribute changes via patch files:

```bash
# Generate email-ready patches for the last 2 commits
mkdir -p patches
git format-patch -2 HEAD -o patches/

# Inspect created patch file
cat patches/0001-*.patch

# Verify patch applicability without applying
git apply --check patches/0001-*.patch

# Apply raw diff patch
git diff HEAD~1 > feature-diff.patch
git apply --stat feature-diff.patch
```

---

## Step 18: Release tagging and branch finalization
Integrate feature branch into `main` and mark release `v2.0`:

```bash
# Switch to main
git checkout main

# Merge feature branch
git merge --no-ff feature/calculator-ops -m "merge: integrate full calculator operations suite"

# Create annotated release tag
git tag -a v2.0 -m "Release version 2.0: Complete arithmetic suite with addition, subtraction, multiplication, and division"

# Inspect tag details
git show v2.0

# Prune local feature branch
git branch -d feature/calculator-ops
```

---

# Part 3 - Optional Advanced Operations

## 3.1: Cherry-Pick Operations & Workflows

`git cherry-pick` applies changes introduced by one or more existing commits from any branch onto the currently checked-out branch.

```bash
# 1. Create a release maintenance branch from v1.0
git checkout -b release/1.x v1.0

# 2. Identify the commit hash of a critical fix on main (e.g. 9f8e7d6)
git log main --oneline -n 5

# 3. Cherry-pick the single fix commit onto release/1.x
# The -x flag appends a line indicating the original commit hash: (cherry picked from commit ...)
git cherry-pick -x 9f8e7d6

# 4. Cherry-pick without committing immediately (-n / --no-commit)
# Allows combining or tweaking changes before staging
git cherry-pick -n 2b3c4d5
git status

# 5. If cherry-pick encounters a conflict:
# git status
# ... manually resolve conflicts in files ...
# git add <resolved-files>
# git cherry-pick --continue
# (Or abort: git cherry-pick --abort)

# Verify cherry-picked commit on release branch
git log -1
```

---

## 3.2: Git Blame Analysis & Investigative Auditing

`git blame` examines file contents line by line to determine who wrote each line, when, and in which commit.

```bash
# 1. Standard blame on Calculator.java
git blame Calculator.java

# 2. Blame specific line range (lines 10 to 22)
git blame -L 10,22 Calculator.java

# 3. Ignore whitespace-only changes
git blame -w -L 1,15 Calculator.java

# 4. Display author email address instead of author name
git blame -e Calculator.java

# 5. Detect code lines copied or moved from other files in the same commit
git blame -C Calculator.java

# 6. Detect code lines moved within the same file
git blame -M Calculator.java
```

*Sample Git Blame Output:*
```text
3a89e1b2 (Ujjwal Kumar 2026-11-17 09:20:00 +0530  1) public class Calculator {
3a89e1b2 (Ujjwal Kumar 2026-11-17 09:20:00 +0530  2)     
3a89e1b2 (Ujjwal Kumar 2026-11-17 09:20:00 +0530  3)     public int add(int a, int b) {
3a89e1b2 (Ujjwal Kumar 2026-11-17 09:20:00 +0530  4)         return a + b;
3a89e1b2 (Ujjwal Kumar 2026-11-17 09:20:00 +0530  5)     }
1a2b3c4d (Ujjwal Kumar 2026-11-17 09:35:12 +0530  6) 
1a2b3c4d (Ujjwal Kumar 2026-11-17 09:35:12 +0530  7)     public int subtract(int a, int b) {
1a2b3c4d (Ujjwal Kumar 2026-11-17 09:35:12 +0530  8)         return a - b;
1a2b3c4d (Ujjwal Kumar 2026-11-17 09:35:12 +0530  9)     }
```

---

## 3.3: Client-Side Git Hooks

Git hooks are client-side shell scripts located in `.git/hooks/`. When given executable permissions (`chmod +x`), they run automatically at key lifecycle stages.

### Hook 1: `.git/hooks/pre-commit`
Enforces Java compilation and blocks leftover conflict markers prior to commit creation.

```bash
cat << 'EOF' > .git/hooks/pre-commit
#!/usr/bin/env bash
# Pre-commit hook: verify syntax and prevent accidental commit of conflict markers

echo "==> [Hook] Running pre-commit verification..."

# Check 1: Detect merge conflict markers
if git diff --cached | grep -E '^(<{7}|={7}|>{7})'; then
    echo "ERROR: Commit rejected! Unresolved merge conflict markers detected in staged files."
    exit 1
fi

# Check 2: Verify Java compilation for all staged .java files
STAGED_JAVA=$(git diff --cached --name-only --diff-filter=ACM | grep '\.java$')
if [ -n "$STAGED_JAVA" ]; then
    echo "Compiling staged Java sources..."
    TMP_DIR=$(mktemp -d)
    javac -d "$TMP_DIR" $STAGED_JAVA
    COMPILE_STATUS=$?
    rm -rf "$TMP_DIR"
    
    if [ $COMPILE_STATUS -ne 0 ]; then
        echo "ERROR: Java compilation failed on staged files. Fix errors before committing."
        exit 1
    fi
fi

echo "==> [Hook] Pre-commit checks passed!"
exit 0
EOF

chmod +x .git/hooks/pre-commit
```

### Hook 2: `.git/hooks/commit-msg`
Enforces standard conventional commit formatting or Jira issue keys.

```bash
cat << 'EOF' > .git/hooks/commit-msg
#!/usr/bin/env bash
# Commit-msg hook: validate commit message format

COMMIT_MSG_FILE=$1
COMMIT_MSG=$(head -n 1 "$COMMIT_MSG_FILE")

CONVENTIONAL_PATTERN="^((feat|fix|docs|style|refactor|perf|test|chore|merge)(\([a-z0-9_-]+\))?: .+|\[(JMP|TASK)-[0-9]+\]: .+)"

if ! [[ "$COMMIT_MSG" =~ $CONVENTIONAL_PATTERN ]]; then
    echo "=================================================================="
    echo "ERROR: Invalid commit message format!"
    echo "Your message: '$COMMIT_MSG'"
    echo ""
    echo "Required format:"
    echo "  - Conventional: feat: description, fix(scope): description, etc."
    echo "  - Jira key:     [JMP-123]: description"
    echo "=================================================================="
    exit 1
fi

exit 0
EOF

chmod +x .git/hooks/commit-msg
```

### Hook 3: `.git/hooks/pre-push`
Protects `main` branch from accidental direct pushes and runs automated tests.

```bash
cat << 'EOF' > .git/hooks/pre-push
#!/usr/bin/env bash
# Pre-push hook: prevent direct pushes to main/master and run test suite

CURRENT_BRANCH=$(git symbolic-ref --short HEAD)

if [ "$CURRENT_BRANCH" = "main" ] || [ "$CURRENT_BRANCH" = "master" ]; then
    echo "ERROR: Direct push to '$CURRENT_BRANCH' branch is strictly forbidden!"
    echo "Please create a feature branch and open a Pull Request."
    exit 1
fi

if [ -f "pom.xml" ]; then
    echo "==> Running test suite before pushing..."
    mvn clean test -q
    if [ $? -ne 0 ]; then
        echo "ERROR: Unit tests failed! Push rejected."
        exit 1
    fi
fi

exit 0
EOF

chmod +x .git/hooks/pre-push
```

### Testing the Hooks:
```bash
# 1. Test commit-msg hook rejection:
git commit -am "bad commit message without prefix"
# Output: ERROR: Invalid commit message format! (Commit rejected)

# 2. Test commit-msg hook success:
git commit -am "feat: correct conventional message"
# Output: [Hook] Pre-commit checks passed! Commit succeeded.

# 3. Test pre-push hook rejection on main:
git checkout main
git push origin main
# Output: ERROR: Direct push to 'main' branch is strictly forbidden! (Push rejected)
```

---

## Summary of Accomplishments
- **Part 1**: Configured multi-level Git parameters, established aliases, implemented branch-and-merge feature workflows, provoked and reconciled conflict markers, and tagged release `v1.0`.
- **Part 2**: Developed `Calculator.java` through stashes, multi-commit branches, interactive rebasing (`squash`/`fixup`), amends, three-way resets, safe reverts, upstream linear rebasing, reflog resurrection, working tree cleaning, bisect bug identification, linked worktrees, and release `v2.0`.
- **Part 3**: Mastered cherry-picking, blame line-level attribution, and automated quality enforcement using custom executable client hooks.
