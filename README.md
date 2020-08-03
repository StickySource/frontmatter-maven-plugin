frontmatter-maven-plugin
===================

A maven plugin to manipulate simple key value yaml Frontmatter in Markdown pages

Give an example markdown file

```
---
title: A very nice page
date: 2020-07-31
---

some content here
```

## Remove an entry

You can use the deleteFrontmatterRule to remove an entry

      <plugin>
       <groupId>net.stickycode.plugins</groupId>
       <artifactId>frontmatter-maven-plugin</artifactId>
       <version>1.1</version>
       <configuration>
        <rules>
          <deleteFrontmatterRule>
            <key>date</key>
          </deleteFrontmatterRule>
        </rules>
       </configuration>
      </plugin>


The result is like this

```
---
title: A very nice page
---

some content here
```

## Add an entry

You can use the addFrontmatterRule to add a key value pait

     <plugin>
       <groupId>net.stickycode.plugins</groupId>
       <artifactId>frontmatter-maven-plugin</artifactId>
       <version>1.1</version>
       <configuration>
        <rules>
          <addFrontmatterRule>
            <key>project</key>
            <value>${project.artifactId}</value>
          </addFrontmatterRule>
        </rules>
       </configuration>
      </plugin>


```
---
title: A very nice page
date: 2020-07-31
project: the-artifact
---

some content here
```

## Change the output directory

The default output directory is ${project.build.directory}/markdown

     <plugin>
       <groupId>net.stickycode.plugins</groupId>
       <artifactId>frontmatter-maven-plugin</artifactId>
       <version>1.1</version>
       <configuration>
         <outputDirectory>target/some/directory</outputDirectory>
       </configuration>
     </plugin>


## Change the source directory

The default source directory is src/main/markdown

     <plugin>
       <groupId>net.stickycode.plugins</groupId>
       <artifactId>frontmatter-maven-plugin</artifactId>
       <version>1.1</version>
       <configuration>
         <targetDirectory>target/some/directory</targetDirectory>
       </configuration>
     </plugin>


