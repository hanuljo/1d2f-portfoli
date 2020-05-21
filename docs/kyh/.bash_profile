# generated by Git for Windows
test -f ~/.profile && . ~/.profile
test -f ~/.bashrc && . ~/.bashrc

alias clean='gradle cleanEclipse && gradle eclipse'

color_option="%C(cyan)%h%C(reset) - %C(green)(%ar)%C(reset) %C(white)%s%C(reset) %C(dim white)- %an%C(reset)%C(yellow)%d%C(reset)"
alias log="git log --graph --abbrev-commit --decorate --format=format:'$color_option' --all"

alias init="rm -rf ./1d2f-portfoli/ && git clone https://github.com/yh0921k/1d2f-portfoli.git && cd ./1d2f-portfoli && clean"
alias commit="git commit --verbose"
alias push="git push origin master"
alias status="git status -s"
alias delete="git branch -D kyh-dev"
alias checkout="git checkout -b kyh-dev"
alias master="git checkout master"
alias dev="git checkout kyh-dev"
alias merge="git merge kyh-dev --no-ff"
alias pull="git pull"
alias add="git add ."
