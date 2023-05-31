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
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	// implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
	implementation("org.flywaydb:flyway-core")
	runtimeOnly("org.postgresql:postgresql")

	annotationProcessor("org.projectlombok:lombok")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

	implementation(libs.mapstruct)
	annotationProcessor(libs.mapstruct.processor)
	testAnnotationProcessor(libs.mapstruct.processor)

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:junit-jupiter:1.18.2")
	testImplementation("org.testcontainers:postgresql:1.18.2")
	testImplementation("org.testcontainers:testcontainers:1.18.2")
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
