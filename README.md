Travianize
==========

Travianize is free opensource bot for [Travian](http://travian.com) T4.

**Сurrent version: 0.0.5**

## Features

* Upgrading resource field and building (not creating)

## How to

1. Create file 'accounts.conf' or start bot, it's created automatically.
2. Write your account information **host:login:pass**. One account in each line
3. Create file 'task_%server%_%login%.txt' for each account, or start bot, it's created automatically.
4. Write tasks in the created file. For resource filed use 'r:%id%', for building 'b:%id%'.

File example:
```
r:2
r:4
b:35
b:30
```