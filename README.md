# Apoj-09-2017 [![Build Status](https://travis-ci.org/java-park-mail-ru/Apoj-09-2017.svg?branch=develop)](https://travis-ci.org/java-park-mail-ru/Apoj-09-2017)

[Heroku](https://apoj.herokuapp.com/)

* [Описание](#description)
* [Команда](#team)
  * [Frontend](#frontend)
  * [Backend](#backend)
* [API](#API)

<a name="description"></a>
## Описание
> Apoj - игра для ценителей приятной музыки и хороших шуток. Первый игрок поет песню, затем она переворачивается, дается на прослушивание второму игроку. Он по фрагментам воссоздает запись своим пением и полученное снова переворачивают. По результату и необходимо отгадать изначальную мелодию. 

<a name="team"></a>
## Команда
<a name="frontend"></a>
### Frontend
  * [Владимиир Пряхин](http://github.com/pryahin)
  * [Свойкина Надежда](http://github.com/couatl)
  
<a name="backend"></a>
### Backend
  * [Москалев Илья](http://github.com/ilyamoskalev)
  * [Кудинов Михаил](http://github.com/MikKud)
  
<a name="API"></a>
## API

|  | url | Тело запроса |
| ------ | ------ | ------ |
| Зарегистрироваться | signup | ```{"login":"abas", "password":"1834240", "email":"asdf@mail.ru"}```
| Авторизоваться | signin | ```{"login":"abas", "password":"1834240"}```
| Запросить пользователя текущей сессии | user | -
| Изменить пароль | newpswrd | ```{"password":"1834240", "change":"1834241"}```
| Изменить логин | newlogin | ```{"password":"1834240", "change":"asdfg"}```
| Изменить email | newemail | ```{"password":"1834240", "change":"asdf@mail.ru"}```
| Разлогиниться | logout | ```{}```

