*** Для корректной работы приложения должен быть поднят: ***

1. Сервер Minio. В application.yml вынесены: minio url, access key и secret key.
2. Сервер Redis. В application.yml вынесены: host и port сервера.
3. Сервер PostgreSQL. В application.yml вынесены все настройки.

*** Функционал взаимодействия с Minio ***

1. Получение файла
    Пример http-запроса GET http://localhost:8011/storage/minio/getFile?bucket=my.bucket&path=test.txt,
    где bucket: имя бакета (предварительно должен быть создан), path: путь до файла и имя самого файла
2. Получение мета-информации
    Пример http-запроса GET http://localhost:8011/storage/minio/getMeta?bucket=my.bucket&path=test.txt,
    где bucket: имя бакета (предварительно должен быть создан), path: путь до файла и имя самого файла
3. Сохранение файла на Minio
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
4. Удаление файла
    Пример http-запроса DELETE http://localhost:8011/storage/minio/deleteFile?bucket=my.bucket&path=test.txt,
    где bucket: имя бакета (предварительно должен быть создан), path: путь до файла и имя самого файла

*** Функционал взаимодействия с PostgreSQL *** (в разработке)

1. Создание
    Пример http-запрос PUT http://localhost:8011/storage/customer/create,
    Пример payload запроса:
    {
        	"first-name" : "nameValue",
        	"last-name" : "lastNameValue",
    }
2. Множественное создание
    Пример http-запрос PUT http://localhost:8011/storage/customer/bulkCreate,
    Пример payload запроса:
    {
        "bulk" : [
            {
       	        "first-name" : "nameValue",
       	        "last-name" : "lastNameValue",
       	    },
       	    {
                "first-name" : "nameValue",
                "last-name" : "lastNameValue",
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

1. Создание jar файла и docker image: mvn clean install package docker:build

