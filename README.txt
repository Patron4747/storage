*** Для корректной работы приложения должен быть поднят: ***

1. Сервер Minio. В application.yml вынесены: minio url, access key и secret key.
2. Сервер Redis. В application.yml вынесены: host и port сервера.
3. Сервер PostgreSQL. В application.yml вынесены все настройки.

*** Функционал взаимодействия с Minio ***

1. Создание бакета
    Пример http-запроса POST http://localhost:8011/storage/minio/createBucket,
    Пример payload запроса:
    {
    	"name" : "my.bucket"
    }
2. Получение файла
    Пример http-запроса GET http://localhost:8011/storage/minio/getFile?bucket=my.bucket&path=test.txt,
    где bucket: имя бакета (предварительно должен быть создан), path: путь до файла и имя самого файла
3. Получение мета-информации
    Пример http-запроса GET http://localhost:8011/storage/minio/getMeta?bucket=my.bucket&path=test.txt,
    где bucket: имя бакета (предварительно должен быть создан), path: путь до файла и имя самого файла
4. Сохранение файла на Minio
    Пример http-запроса PUT http://localhost:8011/storage/minio/uploadFile,
    Пример payload запроса:
    {
    	"bucket" : "my.bucket",                 // должен быть предварительно создан
    	"path" : "my/path/uploadedFile.txt",    // путь до файла может быть предварительно не создан
    	"meta-info" : {
    		"metaKey1" : "myMetaValue1",
    		"metaKey2" : "myMetaValue2"
    	},
    	"bytes" : [56,56,56,78,89,45]
    }
5. Удаление файла
    Пример http-запроса DELETE http://localhost:8011/storage/minio/deleteFile?bucket=my.bucket&path=test.txt,
    где bucket: имя бакета (предварительно должен быть создан), path: путь до файла и имя самого файла

*** Функционал взаимодействия с PostgreSQL *** (в разработке)

1. Создание
    Пример http-запрос PUT http://localhost:8011/storage/customer/create,
    Пример payload запроса:
    {
        	"first-name" : "nameValue",
        	"last-name" : "lastNameValue"
    }
2. Множественное создание
    Пример http-запрос PUT http://localhost:8011/storage/customer/bulkCreate,
    Пример payload запроса:
    {
        "bulk" : [
            {
       	        "first-name" : "nameValue",
       	        "last-name" : "lastNameValue"
       	    },
       	    {
                "first-name" : "nameValue",
                "last-name" : "lastNameValue"
            },
       	]
    }
3. Получение по id
    Пример http-запрос GET http://localhost:8011/storage/customer/getById?id=12
4. Получение по полю
    Пример http-запрос GET http://localhost:8011/storage/customer/getByFirstName?firstName=myValue
5. Получение всех записей
    Пример http-запрос GET http://localhost:8011/storage/customer/getAll
6. Удаление по id
    Пример http-запрос GET http://localhost:8011/storage/customer/deleteById?id=12

*** Docker ***

1. Приложение настроено для запуска при помощи единственной команды docker-compose up, которой нужно
    "скормить" файл docker-compose.yml
2. Все вышеперечисленные запросы доступны через хост докера 192.168.99.100,
   Пример http-запрос GET http://192.168.99.100:8011/storage/customer/getById?id=12
