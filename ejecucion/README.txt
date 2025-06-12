
Este proyecto se ha desarrollado en JAVA 17 mediante SpringBoot y base de datos H2 para el almacenamiento en memoria

INSTRUCCIONES DE COMPILACION
Para poder compilar el proyecto se debe:
* Abrir una ventana de comandas en la raiz del proyecto donde esté el archivo pom.xml
* Ejecutar en el siguiente orden:
	mvn clean install
	mvn clean package spring-boot:repackage	

INSTRUCCIONES DE EJECUCION (en entorno IDE)
Para poder ejecutar el proyecto se debe:
* Abrir una ventana de comandas en la raiz del proyecto donde esté el archivo pom.xml
* Ejecutar en el siguiente orden:
	mvn clean install
	mvn spring-boot:run
	
Una vez ejecutado el microservicio, se puede probar mediante el siguiente enlace: http://localhost:8080/atm


INSTRUCCIONES DE EJECUCION (por comandos)
Para poder ejecutar el proyecto se debe:
* Se requiere del empaquetado completo del paso anterior 'microservicio-atm-1.0.0.jar'
* Modificar el archivo .bat disponible 'lanzar.bat'
	Se debe ajustar el valor de JAVA_HOME para que use el JDK necesario (se ha usado un JDK 17 de ZULU)
	Se debe ajustar la ruta del donde se dispone del compilado por ejemplo: C:\Usuario\Proyectos\ProyectoATM\microservicio-atm-1.0.0.jar
	
Una vez ejecutado el microservicio, se puede probar mediante el siguiente enlace: http://localhost:8080/atm
