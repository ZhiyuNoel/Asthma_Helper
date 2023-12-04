# Asthma_Helper

## Some Learning Materials:
There is a tutorial of vue springboot full-stack development, but in Chinese, I recommend everyone to have a view, it's quite simple:
[Tutorial](https://www.cnblogs.com/GoodHelper/p/8430422.html)

## Some recommendations:
1. Please do not do some operations (e.g. add branch, change code), one the webpage directly.
2. Merging operations, creating new branches, and modifying code are all permitted as long as you want to, but please process the conflict well if happened. And please write more details about what you have add, and what you have changed when commit.
3. If you have met any questions, bugs, you cannot solve, please create issues in Issue page (Between Code page and Pull request). In issue, please lable out which branch and which file and even which line.
4. To be continue ...

## IF YOU ARE TAKING RESPONSIBILITY FOR INTERFACE
!! Please create your own branch firstly before doing any change.
Before create your branch, you need to change your branch to interface first, and then create branches:
```
1. git checkout interface
2. git branch <your_branch_name>
```

Then if your change is able to merge to the interface: 
1. Change the branch to interface 
```
git checkout interface
```
2. Merge the target branch to branch "interface"
```
git merge dev
```

## IF YOU ARE WORKING FOR BACKEND
!! The same, create your own branch before, but under branch backend:
```
1. git checkout backend
2. git branch <your_own_branch>
```