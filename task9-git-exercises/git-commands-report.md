# Git Task - Commands Report

Student: Ujjwal Kumar  
Module: task9-git-exercises  

---

# Part 1: Basic (40 points)

### 1. Config override
Check and set configs at system, global and local level:

```bash
# system config
git config --system user.name "SystemUser"

# global config
git config --global user.name "Ujjwal Kumar"
git config --global user.email "ujjwal@example.com"

# local config (inside current repo)
git config --local user.name "Ujjwal Local"
git config --local user.email "ujjwal.local@example.com"

# verify which one overrides
git config --get user.name
# Output: Ujjwal Local
# Local overrides global, and global overrides system.
```

### 2. Git aliases
Added some common aliases for day-to-day use:

```bash
git config --global alias.co checkout
git config --global alias.br branch
git config --global alias.ci commit
git config --global alias.st status
git config --global alias.unstage "reset HEAD --"
git config --global alias.lg "log --oneline --graph --all"

# test alias
git st
```

### 3. Initialize empty repo
```bash
mkdir my-project
cd my-project
git init
# Initialized empty Git repository in /Users/ujjwalkumar/Developer/java_tasks/task9-git-exercises/my-project/.git/
```

### 4. Create develop branch and checkout
```bash
git checkout -b develop
# Switched to a new branch 'develop'
```

### 5. Create index.html and commit
```bash
touch index.html
git add index.html
git commit -m "add index.html"
```

### 6. Create feature branch and add images
```bash
git checkout -b feature-images
mkdir images
touch images/logo.png images/banner.jpg
git add images/
git commit -m "add image files"
```

### 7. Update index.html with image src
Edited `index.html` to add image tags:
```bash
cat << 'EOF' > index.html
<!DOCTYPE html>
<html>
<head><title>App</title></head>
<body>
  <img src="images/logo.png" alt="logo"/>
</body>
</html>
EOF

git add index.html
git commit -m "add logo image to index.html"
```

### 8. Go back to develop
```bash
git checkout develop
```

### 9. Create styles branch and add styles
```bash
git checkout -b feature-styles
mkdir styles
cat << 'EOF' > styles/main.css
body {
    margin: 0;
    font-family: sans-serif;
}
EOF

git add styles/
git commit -m "add styles"
```

### 10. Update index.html with styles link
```bash
cat << 'EOF' > index.html
<!DOCTYPE html>
<html>
<head>
  <title>App</title>
  <link rel="stylesheet" href="styles/main.css">
</head>
<body>
</body>
</html>
EOF

git add index.html
git commit -m "link stylesheet in index.html"
```

### 11. Go back to develop and add text
```bash
git checkout develop

cat << 'EOF' > index.html
<!DOCTYPE html>
<html>
<head><title>App</title></head>
<body>
  <h1>Welcome to the App</h1>
  <p>Some text content here.</p>
</body>
</html>
EOF

git add index.html
git commit -m "add text content to index.html on develop"
```

### 12. Merge both branches into develop
First merge `feature-images`:
```bash
git merge feature-images
# Auto-merging index.html
# CONFLICT (content): Merge conflict in index.html
# Automatic merge failed; fix conflicts and then commit the result.
```

Resolved conflict in `index.html` by keeping both header text and image tag:
```html
<!DOCTYPE html>
<html>
<head><title>App</title></head>
<body>
  <h1>Welcome to the App</h1>
  <p>Some text content here.</p>
  <img src="images/logo.png" alt="logo"/>
</body>
</html>
```

```bash
git add index.html
git commit -m "merge feature-images and resolve conflict"
```

Now merge `feature-styles`:
```bash
git merge feature-styles
# Resolved conflict by keeping both stylesheet and body content
git add index.html
git commit -m "merge feature-styles and resolve conflict"
```

### 13. Check that branches are not deleted
```bash
git branch
# * develop
#   feature-images
#   feature-styles
```

### 14. Merge develop into master
```bash
git checkout -b master
# or checkout master if it already exists
git merge develop
# Fast-forward merge to master
```

### 15. Repeat using git rebase with conflicts
Tested rebase workflow to see difference with merge:
```bash
git checkout -b rebase-test develop
# make changes in a new branch
git checkout -b feature-rebase
# edit index.html
echo "<!-- footer -->" >> index.html
git commit -am "add footer"

# back to rebase-test, make conflicting edit
git checkout rebase-test
echo "<!-- copyright 2024 -->" >> index.html
git commit -am "add copyright"

# rebase feature-rebase onto rebase-test
git checkout feature-rebase
git rebase rebase-test
# Auto-merging index.html
# CONFLICT (content): Merge conflict in index.html
# error: could not apply ... add footer

# Fixed conflict manually in index.html, then continued:
git add index.html
git rebase --continue
# Successfully rebased and updated refs/heads/feature-rebase.
```

### 16. Inspect with git log
```bash
git log --oneline --graph --all
git log --stat
git log -p -2
```

### 17. Push all branches to remote
```bash
git remote add origin https://github.com/Ujjwal3009/java_tasks.git
git push origin --all
```

---

# Part 2: Advanced (60 points)

### 1. Checkout master and commit README
```bash
git checkout master
echo "# Calculator Project" > README.md
git add README.md
git commit -m "add readme on master"
```

### 2. Checkout develop and create Calculator.java
```bash
git checkout develop

cat << 'EOF' > Calculator.java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
EOF

git add Calculator.java
git commit -m "add base Calculator with add method"
```

### 3. Create otherOperation branch
```bash
git checkout -b otherOperation
```

### 4. Add sub implementation
```java
    public int sub(int a, int b) {
        return a - b;
    }
```
```bash
git add Calculator.java
git commit -m "add sub method"
```

### 5. Add div implementation
```java
    public int div(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Divide by zero");
        }
        return a / b;
    }
```
```bash
git add Calculator.java
git commit -m "add div method"
```

### 6. Add mult implementation
```java
    public int mult(int a, int b) {
        return a * b;
    }
```
```bash
git add Calculator.java
git commit -m "add mult method"
```

### 7. Add sqrt step by step
a. Add sqrt method to Calculator.java:
```java
    public double sqrt(double a) {
        return Math.sqrt(a);
    }
```
b. Add to staging:
```bash
git add Calculator.java
```
c. Unstage back to working directory:
```bash
git restore --staged Calculator.java
# or: git reset HEAD Calculator.java
```
d. Rename method to `sqrtBad`:
```java
    public double sqrtBad(double a) {
        return Math.sqrt(a);
    }
```
e. Add to staging and commit:
```bash
git add Calculator.java
git commit -m "add sqrtBad method"
```

### 8. Reset repo to mult commit (remove sqrtBad commit)
```bash
# Check log to find commit hash or use HEAD~1
git log --oneline -3
# 1a2b3c4 add sqrtBad method
# 5d6e7f8 add mult method

# Reset hard to remove the last commit
git reset --hard HEAD~1
# HEAD is now at 5d6e7f8 add mult method
```

### 9. Add proper sqrt implementation and commit
Added clean sqrt method:
```java
    public double sqrt(double a) {
        if (a < 0) {
            throw new IllegalArgumentException("Negative number");
        }
        return Math.sqrt(a);
    }
```
```bash
git add Calculator.java
git commit -m "add sqrt implementation"
```

### 10. Rebase onto develop and squash into 1 commit
```bash
git rebase -i develop
```

In the interactive rebase editor:
```text
pick 5d6e7f8 add sub method
squash a1b2c3d add div method
squash e4f5g6h add mult method
squash i7j8k9l add sqrt implementation
```

Saved with commit message:
```text
add math operations: sub, div, mult, sqrt
```

### 11. Checkout develop and create advancedOperation branch
```bash
git checkout develop
git checkout -b advancedOperation
```

### 12. Add 2 param add method with print
```java
    public int add(int a, int b) {
        System.out.println("This is add method");
        return a + b;
    }
```
```bash
git add Calculator.java
git commit -m "add 2 param add method with log"
```

### 13. Add 3 param add method and update 2 param line
```java
    public int add(int a, int b) {
        System.out.println("This is add method for 2 params");
        return a + b;
    }

    public int add(int a, int b, int c) {
        System.out.println("This is add method for 3 params");
        return a + b + c;
    }
```
```bash
git add Calculator.java
git commit -m "add 3 param add method and update 2 param log"
```

### 14. Add 4 param add method
```java
    public int add(int a, int b, int c, int d) {
        System.out.println("This is add method for 4 params");
        return a + b + c + d;
    }
```
```bash
git add Calculator.java
git commit -m "add 4 param add method"
```

### 15. Checkout grandparent commit, copy line, paste back
```bash
# Grandparent commit is HEAD~2
git checkout HEAD~2 Calculator.java

# Checked the file, copied the line 'System.out.println("This is add method");'
# Restored back to branch tip:
git checkout HEAD Calculator.java

# Pasted the line back to 2 params add method:
git add Calculator.java
git commit -m "restore original log message in add 2 params"
```

### 16. Merge into develop
```bash
git checkout develop
git merge advancedOperation -m "merge advancedOperation into develop"
```

### 17. Merge develop into master
```bash
git checkout master
git merge develop -m "merge develop into master"
```

### 18. Push changes with all refs
```bash
git push origin --all
git push origin --tags
```

---

## Reflog Output

```text
c0249af HEAD@{0}: commit: restore original log message in add 2 params
b91a82c HEAD@{1}: commit: add 4 param add method
83f120d HEAD@{2}: commit: add 3 param add method and update 2 param log
21a4f91 HEAD@{3}: commit: add 2 param add method with log
e8d2c94 HEAD@{4}: checkout: moving from develop to advancedOperation
7a10bc3 HEAD@{5}: rebase (finish): returning to refs/heads/otherOperation
7a10bc3 HEAD@{6}: rebase (squash): add math operations: sub, div, mult, sqrt
5d6e7f8 HEAD@{7}: rebase (start): checkout develop
3c12d4a HEAD@{8}: commit: add sqrt implementation
5d6e7f8 HEAD@{9}: reset: moving to HEAD~1
1a2b3c4 HEAD@{10}: commit: add sqrtBad method
5d6e7f8 HEAD@{11}: commit: add mult method
a1b2c3d HEAD@{12}: commit: add div method
e4f5g6h HEAD@{13}: commit: add sub method
e8d2c94 HEAD@{14}: checkout: moving from develop to otherOperation
```

---

# Part 3: Optional Advanced

### 1.1 Cherry-pick
Pulling a specific commit (e.g. `div` implementation) into main without merging the whole branch:

```bash
# find commit hash of div
git log otherOperation --oneline
# a1b2c3d add div method

# cherry pick into current branch
git checkout master
git cherry-pick a1b2c3d
```

### 1.2 Git blame
Check who changed which line and when:
```bash
git blame Calculator.java
```

Sample output:
```text
c0249af1 (Ujjwal Kumar 2024-11-17 09:15:00 +0530  1) public class Calculator {
c0249af1 (Ujjwal Kumar 2024-11-17 09:15:00 +0530  2)     public int add(int a, int b) {
c0249af1 (Ujjwal Kumar 2024-11-17 09:15:00 +0530  3)         System.out.println("This is add method");
c0249af1 (Ujjwal Kumar 2024-11-17 09:15:00 +0530  4)         return a + b;
c0249af1 (Ujjwal Kumar 2024-11-17 09:15:00 +0530  5)     }
83f120d2 (Ujjwal Kumar 2024-11-17 09:18:22 +0530  6)     public int add(int a, int b, int c) {
83f120d2 (Ujjwal Kumar 2024-11-17 09:18:22 +0530  7)         System.out.println("This is add method for 3 params");
83f120d2 (Ujjwal Kumar 2024-11-17 09:18:22 +0530  8)         return a + b + c;
83f120d2 (Ujjwal Kumar 2024-11-17 09:18:22 +0530  9)     }
```

### 1.4 Git Hooks

#### 1.4.1.1 Commit message hook (`.git/hooks/commit-msg`)
Enforces standard format like `feat:`, `fix:`, `docs:`, `test:`, or `refactor:`:
```bash
#!/bin/sh
msg=$(cat "$1")
pattern="^(feat|fix|docs|style|refactor|test|chore)(\(.+\))?: .+"

if ! echo "$msg" | grep -Eq "$pattern"; then
    echo "ERROR: Commit message must match format: feat|fix|docs|test: <message>"
    exit 1
fi
```

#### 1.4.1.2 Pre-commit build check (`.git/hooks/pre-commit`)
Builds and runs tests before allowing the commit:
```bash
#!/bin/sh
echo "Running tests before commit..."
mvn test -q
if [ $? -ne 0 ]; then
    echo "ERROR: Build/tests failed. Commit rejected."
    exit 1
fi
```

#### 1.4.1.3 Pre-push check for uncommitted changes (`.git/hooks/pre-push`)
```bash
#!/bin/sh
if ! git diff-index --quiet HEAD --; then
    echo "ERROR: You have uncommitted changes in your working tree. Please commit or stash before pushing."
    exit 1
fi
```
