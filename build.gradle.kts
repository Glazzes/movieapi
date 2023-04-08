plugins {
	java
	pmd
	checkstyle
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	id("com.google.cloud.tools.jib") version "3.3.1"
}

group = "com.glaze"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
		vendor.set(JvmVendorSpec.AMAZON)
	}
}

pmd {
	isConsoleOutput = true
	rulesMinimumPriority.set(5)
	sourceSets = listOf(
		project.sourceSets.main.get(),
		project.sourceSets.test.get()
	)
	ruleSets = listOf()
	ruleSetFiles = files(
		"$projectDir/config/pmd/best-practices.xml",
		"$projectDir/config/pmd/performance.xml",
		"$projectDir/config/pmd/error_prone.xml"
	)
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("com.h2database:h2")
}

jib {
	from {
		image = "amazoncorretto:17-alpine"
	}

	to {
		image = "glazee/${project.name}"
		tags = setOf("latest", "$version")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
