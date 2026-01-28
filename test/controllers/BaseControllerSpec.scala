package controllers

trait BaseControllerSpec {
    System.setProperty("APP_SECRET_KEY", "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef")
    System.setProperty("JDBC_DATABASE_URL", "jdbc:postgresql://localhost:5432/test")
    System.setProperty("JDBC_DATABASE_USERNAME", "test")
    System.setProperty("JDBC_DATABASE_PASSWORD", "testpass")
}
