# UrlShortener
Simple URL shortener service with Spring.

##### Work in progress

TODO:
###### Https
###### Set session cookies
###### Cron
###### caching

###### Persistence Layer:
	- class Log {
			// (regular stuff)
		}
	- class LogRepository {
			// (regular stuff)
		}

###### Service layer:
	- userIdToken SetUser(AuthenticationRequest ar)
	- userIdToken GetUser(AuthenticationRequest ar)
	- void DeleteUser(userIdToken uit)
	- void SetUrlMapping(MappingRequest mr)
	- String GetUrlMapping(MappingRequest mr)
	- userIdToken ValidateUser(AuthenticationRequest ar)
	- String PwdToHash(String pwd)
	- void Logger(String log)
	- void PurgeData()
	
###### User interaction layer:
	
	- Model:
		
		class AuthenticationRequest {
			String userName, password;
			AuthenticationRequest(String userName, String password);
			String getUserName();
			void setUserName(String userName);
			String getPassword();
			void setPassword(String Password);
		}
		class MappingRequest {
			// (regular stuff)
		}
		
	- View:
		- Main page: 
			- links to login, signup if signed out.
			- new mapping form, list of owned mappings, link to sign out
		- Login form
		- Signup form
		
	- Controller:
		- Form GETs
		- Form POSTs
		- No content: Logout and delete user
		- Redirect: GET a valid/invalid mapping, redirecting after logout, delete



