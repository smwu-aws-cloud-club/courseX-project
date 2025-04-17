plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	id ("com.diffplug.spotless") version "6.25.0"
}

group = "com.acc"
version = "0.0.1-SNAPSHOT"
val googleJavaFormatVersion = "1.18.1"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testRuntimeOnly("com.h2database:h2")
}

spotless{
	java {
		target("**/*.java")
		googleJavaFormat(googleJavaFormatVersion)
		importOrder("courseX", "java", "javax", "jakarta", "acc", "com")
		endWithNewline()
		removeUnusedImports()
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register<Copy>("updateGitHooks") {
	from("./scripts/pre-commit")
	into("../.git/hooks")
}

tasks.register<Exec>("makeGitHooksExecutable") {
	commandLine("chmod", "+x", "../.git/hooks/pre-commit")
	dependsOn("updateGitHooks")
}

tasks.named("compileJava") {
	dependsOn("makeGitHooksExecutable")
}
