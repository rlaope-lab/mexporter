#!/bin/bash
export FILTER_BRANCH_SQUELCH_WARNING=1
git filter-branch --env-filter '
OLD_EMAIL="heemang.kim@hoenstai.tech"
CORRECT_NAME="rlaope"
CORRECT_EMAIL="piyrw9754@gmail.com"

if [ "$GIT_COMMITTER_EMAIL" = "$OLD_EMAIL" ]; then
    export GIT_COMMITTER_NAME="$CORRECT_NAME"
    export GIT_COMMITTER_EMAIL="$CORRECT_EMAIL"
fi
if [ "$GIT_AUTHOR_EMAIL" = "$OLD_EMAIL" ]; then
    export GIT_AUTHOR_NAME="$CORRECT_NAME"
    export GIT_AUTHOR_EMAIL="$CORRECT_EMAIL"
fi
' --tag-name-filter cat -- --all
