# Git Task - Task 9

Name: Ujjwal Kumar  
Course: Java Modernization Program  
Module: task9-git-exercises  

## Overview

This module covers the Git tasks divided into 3 parts:
- **Part 1: Basic (40 points)** - git config hierarchy, aliases, branches, merging and rebasing with conflicts.
- **Part 2: Advanced (60 points)** - Calculator operations, staging and unstaging, git reset, interactive rebase with squash, commit log and reflog.
- **Part 3: Optional Advanced** - Cherry-picking commits, git blame, and git hooks.

Full step-by-step commands and outputs are documented in [git-commands-report.md](file:///Users/ujjwalkumar/Developer/java_tasks/task9-git-exercises/git-commands-report.md).

---

## Part 1: Basic Steps Summary

1. **Config override**: Set values in system, global and local config. Local overrides global, global overrides system.
2. **Aliases**: Added shortcuts like `co`, `br`, `st`, `lg`.
3. **Init**: Initialized git repo using `git init`.
4. **Develop branch**: Created and switched to `develop` (`git checkout -b develop`).
5. **index.html**: Created empty `index.html` and committed.
6. **Images branch**: Created branch `feature-images`, added `images/` directory with image files, committed.
7. **Update index.html**: Added `<img src="...">` tag into `index.html`, committed.
8. **Checkout develop**: Returned back to `develop`.
9. **Styles branch**: Created branch `feature-styles`, added `styles/style.css`, committed.
10. **Update index.html**: Added stylesheet link to `index.html`, committed.
11. **Text on develop**: Added heading and paragraph text in `index.html` on `develop`.
12. **Merge branches**: Merged both `feature-images` and `feature-styles` into `develop`. Resolved conflict in `index.html`.
13. **Keep branches**: Kept both branches without deleting.
14. **Merge to master**: Merged `develop` into `master`.
15. **Repeat with rebase**: Repeated steps creating conflict, resolved conflict using `git rebase` instead of merge.
16. **Git log**: Inspected commit history with `git log --oneline --graph --all`.
17. **Push to remote**: Pushed all branches to remote (`git push origin --all`).

---

## Part 2: Advanced Steps Summary

1. **README on master**: Created and committed README on `master`.
2. **Calculator base on develop**: Added `Calculator.java` with `add(int a, int b)` on `develop`.
3. **otherOperation branch**: Created branch `otherOperation`.
4. **sub**: Added `sub(int a, int b)` method, committed.
5. **div**: Added `div(int a, int b)` method, committed.
6. **mult**: Added `mult(int a, int b)` method, committed.
7. **sqrt step by step**:
   - Added `sqrt(double a)` method.
   - Added to staging (`git add`).
   - Unstaged back to working directory (`git restore --staged` or `git reset HEAD`).
   - Renamed method to `sqrtBad`.
   - Added to staging and committed.
8. **Reset**: Used `git reset --hard HEAD~1` to remove the `sqrtBad` commit and go back to `mult`.
9. **Proper sqrt**: Added correct `sqrt(double a)` method and committed.
10. **Rebase & squash**: Rebased onto `develop` and squashed commits into 1 with message `feat: add math operations (sub, div, mult, sqrt)`.
11. **advancedOperation branch**: Created branch from `develop`.
12. **add with 2 params**: Added print statement `"This is add method"`. Committed.
13. **add with 3 params**: Added print `"This is add method for 3 params"`, updated 2 param to `"This is add method for 2 params"`. Committed.
14. **add with 4 params**: Added print `"This is add method for 4 params"`. Committed.
15. **Grandparent commit**: Checked out commit `HEAD~2`, copied line, checked out back to branch tip, updated method and committed.
16. **Merge to develop**: Merged `advancedOperation` into `develop`.
17. **Merge to master**: Merged `develop` into `master`.
18. **Push with refs**: Pushed all refs to remote.
19. **Reflog**: Attached reflog at the end of the report.

---

## Part 3: Optional Advanced

- **1.1 Cherry-pick**: Pulled `div` commit from feature branch into main branch using `git cherry-pick <commit-hash>`.
- **1.2 Git blame**: Inspected file changes with `git blame Calculator.java` to see author and commit per line.
- **1.4 Git hooks**:
  - `commit-msg`: validates commit message format.
  - `pre-commit`: runs `mvn test` before allowing commit.
  - `pre-push`: checks for uncommitted changes before pushing.
