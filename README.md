# Technogise

This project is an implementation of the assignment given by Technogise.

## System Compatibility
This assignment can run on:
* Linux
* MacOs
* Windows

## Usage

```
git clone https://github.com/ravinamotwani11/Technogise
cd library.management
gradle build
gradle bootrun
```  
 
# CRUD
Open postman =>  

To view all the books present in the library, hit the following url.  
method: GET  
URL: 
http://localhost:8080/library/books

To add book in the library, hit the following URL.  
method: POST  
URL:
http://localhost:8080/library/add

To borrow book from the library, hit the following URL.  
method: POST  
URL:
http://localhost:8080/library/borrow

RequestBody:
``` 
{
    "userId": {userId},
    "bookTitle": {bookTitle}
}
``` 
	
	Example->
	{
		"userId": "1",
		"bookTitle": "Guide"
	}

To return book to the library, hit the following URL.  
method: POST  
URL:
http://localhost:8080/library/return

RequestBody:
``` 
{
    "userId": {userId},
    "bookTitle": {bookTitle}
}
``` 
	
	Example->
	{
		"userId": "1",
		"bookTitle": "Guide"
	}

To view the book borrowed from the library, hit the following URL.  
method: POST  
URL:
http://localhost:8080/library/borrowedBooks?userId={userId}

	Example-> 
	http://localhost:8080/library/borrowedBooks?userId=1
 


