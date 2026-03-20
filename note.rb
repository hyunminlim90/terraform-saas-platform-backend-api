=begin






        (마킹 값 치환) ⇢ Command + Shift + F

            taskId
            taskArn
            task_id
            task_arn

            ✨ {{__카멜케이스_AWS제외__}} 로 프로젝트 전체에서 치환한다.
            ✨ sprout 로 프로젝트 전체에서 치환한다.

        » (constants package)

            📁 constants
                ⤷ 📄 AppConstants.class

            ✨ TEXTAREA_PARAMS는 {{__카멜케이스_파라미터명__}} 또는 List.of() 로 지정한다. ⇢ note.rb 파일에서 text 검색

            (vm-java) 📌 서버 마이그레이션의 경우 해당

                public static final String ROOT_PATH = "/home/sysadmin/opentofu/workspace";
                public static final String DOWNLOAD_ROOT_PATH = "/home/sysadmin/opentofu/download";

                ✨ 서버에서 작업 폴더를 만든다.

        » (dto package)

            📁 dto
                ⤷ 📄 ResourceDto.class
                ⤷ 📄 TofuDto.class

            (TofuDto class)

                ✨ version.2 를 활용한다.

                » (레코드 컴포넌트 타입) ⇢ 타입 기반 전체 치환 ⭐

                    (string number textarea) ⇢ □ 포함

                        String 

                    (boolean) ⇢ □ 포함

                        Boolean 

                    (string[] boolean[] number[]) ⇢ ⧉ 포함

                        List<String> 

                    (object)

                        Map<String, String> 

                    » (object[])

                        List<Map<String, String>> 

                        ✝︎ (JavaBeans getter 호출부)

                            e.get{{__파스칼케이스_파라미터명__}}().stream()
                                .map({{__중간_엔티티_클래스명__}}::get{{__파스칼케이스_파라미터명__}})
                                .toList(),

                    » (<string[]>[])

                        List<List<String>> 

                        ✝︎ (JavaBeans getter 호출부)

                            e.get{{__파스칼케이스_파라미터명__}}().stream()
                                .map({{__중간_엔티티_클래스명__}}::get{{__파스칼케이스_파라미터명__}})
                                .toList(),
                        
                    (첫 알파벳 대문자)

                        ✨ 화면 표시와 테라폼 파라미터 입력에는 첫 알파벳 대문자로 사용한다.
                        ✨ api 호출의 경우에만 정식 네이밍 컨벤션을 사용한다.

        » (handler package)

            ✝︎ (abstracts package)

                📁 handler
                    ⤷ 📁 entity
                        ⤷ 📁 abstracts
                            ⤷ 📄 BaseEntity.class
                    
                ✨ 파라미터 version.2 를 활용한다.

                » (파라미터 타입 별 필드 타입) ⇢ 타입 기반 전체 치환 ⭐
                
                    (string number) ⇢ □ 포함

                        private String 

                    (textarea)

                        @Lob
                        @Column(columnDefinition = "LONGTEXT")
                        private String 

                    (boolean) ⇢ □ 포함

                        private Boolean 

                    (string[] boolean[] number[]) ⇢ ⧉ 포함

                        @ElementCollection(fetch = FetchType.EAGER)
                        private List<String> 

                    (object object[] <string[]>[])

                        ✨ TofuEntity.class에 선언한다.

                    (첫 알파벳 대문자)

                        ✨ 화면 표시와 테라폼 파라미터 입력에는 첫 알파벳 대문자로 사용한다.
                        ✨ api 호출의 경우에만 정식 네이밍 컨벤션을 사용한다.

            ✝︎ (entity package)

                » (중간 엔티티 샘플)

                    📁 sample
                        ⤷ 📁 java
                            ⤷ 📄 JoinListEntityGroup.class [🔗 6.3]
                            ⤷ 📄 JoinMapEntityGroup.class [🔗 6.3]

                    (마킹 값 치환)

                        sprout
                        {{__스네이크케이스_파라미터명_단수__}}
                        {{__스네이크케이스_파라미터명_복수__}}
                        {{__파스칼케이스_파라미터명_단수__}}
                        {{__확장_속성명__}}

                    ✨ object[] 와 <string[]>[] 는 중간 엔티티 클래스를 활용한다.
                    ✨ 확장 속성명은 {{__카멜케이스_파라미터명__}} 지정한다.

                📁 handler
                    ⤷ 📁 entity
                        ⤷ 📁 entity
                            ⤷ 📄 TofuEntity.class

                » (파라미터 타입 별 필드 스펙)

                    (object)

                        ✝︎ (마킹 값 치환)

                            sprout
                            {{__스네이크케이스_파라미터명_단수__}}
                            {{__스네이크케이스_파라미터명_복수__}}
                            {{__카멜케이스_파라미터명__}}

                        ✝︎ (필드 스펙)

                            @ElementCollection(fetch = FetchType.EAGER)
                            @CollectionTable(name = "sprout_entity_{{__스네이크케이스_파라미터명_복수__}}", joinColumns = @JoinColumn(name = "sprout_entity_pk"))
                            @MapKeyColumn(name = "{{__스네이크케이스_파라미터명_단수__}}_key")
                            @Column(name = "{{__스네이크케이스_파라미터명_단수__}}_value")
                            @Builder.Default
                            private Map<String, String> {{__카멜케이스_파라미터명__}} = new HashMap<>();

                    (object[])

                        ✝︎ (마킹 값 치환)

                            {{__파스칼케이스_파라미터명_단수__}}
                            {{__파스칼케이스_파라미터명_복수__}}
                            {{__카멜케이스_파라미터명_단수__}}
                            {{__카멜케이스_파라미터명_복수__}}
                            {{__파스칼케이스_확장_속성명__}}

                        ✝︎ (필드 스펙)

                            @OneToMany(mappedBy = "tofuEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
                            @Builder.Default
                            @BatchSize(size = 50)
                            private List<{{__파스칼케이스_파라미터명_단수__}}Group> {{__카멜케이스_파라미터명_복수__}} = new ArrayList<>();

                            @JsonGetter("{{__카멜케이스_파라미터명_복수__}}")
                            public List<Map<String, String>> get{{__파스칼케이스_파라미터명_복수__}}AsMap() {
                                return this.{{__카멜케이스_파라미터명_복수__}}.stream()
                                    .map(({{__카멜케이스_파라미터명_단수__}}Group) -> {{__카멜케이스_파라미터명_단수__}}Group.get{{__파스칼케이스_확장_속성명__}}())
                                    .collect(Collectors.toList());
                            }

                    (<string[]>[])

                        ✝︎ (마킹 값 치환)

                            {{__카멜케이스_파라미터명_복수__}}
                            {{__카멜케이스_파라미터명_단수__}}
                            {{__파스칼케이스_파라미터명_복수__}}
                            {{__파스칼케이스_파라미터명_단수__}}
                            {{__파스칼케이스_확장_속성명__}}

                        ✝︎ (필드 스펙)

                            @OneToMany(mappedBy = "tofuEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
                            @Builder.Default
                            @BatchSize(size = 50)
                            private List<{{__파스칼케이스_파라미터명_단수__}}Group> {{__카멜케이스_파라미터명_복수__}} = new ArrayList<>();

                            @JsonGetter("{{__카멜케이스_파라미터명_복수__}}")
                            public List<List<String>> get{{__파스칼케이스_파라미터명_복수__}}AsList() {
                                return this.{{__카멜케이스_파라미터명_복수__}}.stream()
                                    .map(({{__카멜케이스_파라미터명_단수__}}Group) -> {{__카멜케이스_파라미터명_단수__}}Group.get{{__파스칼케이스_확장_속성명__}}())
                                    .collect(Collectors.toList());
                            }

            ✝︎ (implementations package) ⇢ entity

                📁 handler
                    ⤷ 📁 entity
                        ⤷ 📁 implementations
                            ⤷ 📄 DuplicateEntityBuilder.class
                            ⤷ 📄 EntityBuilder.class
                    
                ✨ 파라미터 version.2 를 활용한다.

                » (DTO 엔티티 매핑) ⇢ 타입 기반 전체 치환 ⭐

                    (string number textarea) ⇢ □ 포함

                        (parameters.path("{{__JSON_필드__}}").asText())

                    (boolean) ⇢ □ 포함

                        (parameters.path("{{__JSON_필드__}}").asBoolean())

                    (object)

                        (parse{{__파스칼케이스_파라미터명__}}(parameters.path("{{__JSON_필드__}}"))) 

                        ✨ 컨버터 메서드를 선언한다. [🔗 6.3]

                    (string[] number[] boolean[]) ⇢ ⧉ 포함

                        (parseToListString(parameters.path("{{__JSON_필드__}}")))
                    
                    (object[])

                        ✝︎ (마킹 값 치환)

                            {{__파스칼케이스_파라미터명_복수__}}
                            {{__파스칼케이스_파라미터명_단수__}}
                            {{__카멜케이스_파라미터명_복수__}}
                            {{__JSON_필드__}}

                        ✝︎ (필드 스펙)

                            List<{{__파스칼케이스_파라미터명_단수__}}Group> {{__카멜케이스_파라미터명_복수__}} = parseTo{{__파스칼케이스_파라미터명_단수__}}GroupList(parameters.path("{{__JSON_필드__}}"), tofuEntity);
                            tofuEntity.set{{__파스칼케이스_파라미터명_복수__}}({{__카멜케이스_파라미터명_복수__}});

                        ✨ build() 아래에 선언한다.
                        ✨ 컨버터 메서드를 선언한다. [🔗 6.3]

                    (<string[]>[])

                        ✝︎ (마킹 값 치환)

                            {{__파스칼케이스_파라미터명_복수__}}
                            {{__파스칼케이스_파라미터명_단수__}}
                            {{__카멜케이스_파라미터명_복수__}}
                            {{__JSON_필드__}}

                        ✝︎ (필드 스펙)

                            List<{{__파스칼케이스_파라미터명_단수__}}Group> {{__카멜케이스_파라미터명_복수__}} = parseTo{{__파스칼케이스_파라미터명_단수__}}GroupList(parameters.path("{{__JSON_필드__}}"), tofuEntity);
                            tofuEntity.set{{__파스칼케이스_파라미터명_복수__}}({{__카멜케이스_파라미터명_복수__}});

                        ✨ build() 아래에 선언한다.
                        ✨ 컨버터 메서드를 선언한다. [🔗 6.3]

            ✝︎ (implementations package) ⇢ python

                📁 handler
                    ⤷ 📁 python
                        ⤷ 📁 implementations
                            ⤷ 📄 AwsEcsTaskDefinitionBoto.class

                ✨ 리전에 하드코딩을 수정한다.

            ✝︎ (implementations package) ⇢ tofu_module

                📁 handler
                    ⤷ 📁 tofu
                        ⤷ 📁 module
                            ⤷ 📁 implementations
                                ⤷ 📄 TofuModule.class

                ✨ 'index_key'에서 unique value를 지정한다.
                ✨ yaml hcl 변환부를 지정한다.

                » (Yaml 필드 매핑) ⇢ 타입 종류대로 지정 속력 개선 ⭐

                    (name)

                        ✨ name 필드는 index_key와 동일한 엔티티로 인식하는데 사용한다.
                        
                    (string boolean number)

                        ✝︎ (마킹 값 치환)

                            {{__스네이크케이스_파라미터명__}}

                        ✝︎ (매핑 코드)

                            definedPythonFile
                                .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                    for (Map<String, Object> resourceConfig : resourceConfigs) {
                                        try {
                                            definedPythonFile
                                                .append("        - \"" + resourceConfig.get("{{__스네이크케이스_파라미터명__}}") + "\"\n");
                                        } catch (Exception error) {
                                            throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                        }
                                    }

                    » (textarea)

                        ✝︎ (shell script)

                            (마킹 값 치환)

                                {{__스네이크케이스_파라미터명__}}
                                {{__카멜케이스_파라미터명__}}
                                {{__카멜케이스_AWS제외__}}
                                sprout

                            (매핑 코드)

                                definedPythonFile
                                    .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                        for (Map<String, Object> resourceConfig : resourceConfigs) {
                                            try {
                                                String {{__카멜케이스_파라미터명__}} = (String) resourceConfig.get("{{__스네이크케이스_파라미터명__}}");
                                                if ({{__카멜케이스_파라미터명__}} == null || {{__카멜케이스_파라미터명__}}.isEmpty()) {
                                                    definedPythonFile
                                                        .append("    - \"\"\n");
                                                } else {
                                                    String {{__카멜케이스_AWS제외__}}Id = (String) resourceConfig.get("sprout_id");
                                                    if (!{{__카멜케이스_AWS제외__}}Id.contains("create-only-")) {
                                                        if ({{__카멜케이스_AWS제외__}}Id.startsWith("arn:")) {
                                                            String resource = {{__카멜케이스_AWS제외__}}Id.split(":", 6)[5];
                                                            String[] tokens = resource.split("[/:]");
                                                            {{__카멜케이스_AWS제외__}}Id = String.join("-", tokens);
                                                        }
                                                    }
                                                    definedPythonFile
                                                        .append("    - \"" + {{__카멜케이스_AWS제외__}}Id + "_{{__카멜케이스_파라미터명__}}.sh\"\n");
                                                }
                                            } catch (Exception error) {
                                                throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                            }
                                        }

                        ✝︎ (Json)

                            (마킹 값 치환)

                                {{__스네이크케이스_파라미터명__}}
                                sprout
                                {{__카멜케이스_파라미터명__}}
                                {{__카멜케이스_AWS제외__}}

                            (매핑 코드)

                                definedPythonFile
                                    .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                        for (Map<String, Object> resourceConfig : resourceConfigs) {
                                            try {
                                                boolean isEmpty = false;
                                                Object value = resourceConfig.get("{{__스네이크케이스_파라미터명__}}");
                                                
                                                if (value == null) { isEmpty = true; }
                                                
                                                else if (value instanceof String) {
                                                    ObjectMapper mapper = new ObjectMapper();
                                                    JsonNode node = mapper.readTree((String) value);
                                                    if ((node.isArray() || node.isObject()) && node.size() == 0) { isEmpty = true; }
                                                
                                                } else if (value instanceof List) {
                                                    if (((List<?>) value).isEmpty()) { isEmpty = true; }
                                                
                                                } else if (value instanceof Map) {
                                                    if (((Map<?, ?>) value).isEmpty()) { isEmpty = true; }
                                                }
                                                    
                                                if (isEmpty) {
                                                    definedPythonFile
                                                        .append("        - \"\"\n");
                                                } else {
                                                    String {{__카멜케이스_AWS제외__}}Id = (String) resourceConfig.get("sprout_id");
                                                    if (!{{__카멜케이스_AWS제외__}}Id.contains("create-only-")) {
                                                        if ({{__카멜케이스_AWS제외__}}Id.startsWith("arn:")) {
                                                            String resource = {{__카멜케이스_AWS제외__}}Id.split(":", 6)[5];
                                                            String[] tokens = resource.split("[/:]");
                                                            {{__카멜케이스_AWS제외__}}Id = String.join("-", tokens);
                                                        }
                                                    }
                                                    definedPythonFile
                                                        .append("        - \"" + {{__카멜케이스_AWS제외__}}Id + "_{{__카멜케이스_파라미터명__}}.json\"\n");
                                                }
                                            } catch (Exception error) {
                                                throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                            }
                                        }

                    (string[] boolean[] number[])

                        ✝︎ (마킹 값 치환)

                            {{__스네이크케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명__}}

                        ✝︎ (매핑 코드)

                            definedPythonFile
                                .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                    for (Map<String, Object> resourceConfig : resourceConfigs) {
                                        try {
                                            @SuppressWarnings("unchecked")
                                            List<Object> {{__카멜케이스_파라미터명__}} = (List<Object>) resourceConfig.get("{{__스네이크케이스_파라미터명__}}");
                                            definedPythonFile
                                                .append("        - \"" + String.join("{{__,__}}", {{__카멜케이스_파라미터명__}}.stream().map(Object::toString).collect(Collectors.toList())) + "\"\n");
                                        } catch (Exception error) {
                                            throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                        }
                                    }

                    (object)

                        ✝︎ (마킹 값 치환)

                            {{__스네이크케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명_단수__}}
                            {{__파스칼케이스_단수_띄어쓰기__}}

                        ✝︎ (매핑 코드)

                            definedPythonFile
                                .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                    for (Map<String, Object> resourceConfig : resourceConfigs) {
                                        try {
                                            @SuppressWarnings("unchecked")
                                            Map<String, Object> {{__카멜케이스_파라미터명__}} = (Map<String, Object>) resourceConfig.get("{{__스네이크케이스_파라미터명__}}");
                                            if ({{__카멜케이스_파라미터명__}} != null && !{{__카멜케이스_파라미터명__}}.isEmpty()) {
                                                String {{__카멜케이스_파라미터명_단수__}}Result = {{__카멜케이스_파라미터명__}}.entrySet().stream() 
                                                    .map((entry) -> { return entry.getKey() + "{{__:__}}" + entry.getValue(); })
                                                    .collect(Collectors.joining("{{__,__}}"));
                                                definedPythonFile.append("        - \"" + {{__카멜케이스_파라미터명_단수__}}Result + "\"\n");
                                            } else {
                                                definedPythonFile
                                                    .append("        - \"\"\n");
                                                log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                                                log.warn("{{__파스칼케이스_단수_띄어쓰기__}} does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                                                log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                                            }
                                        } catch (Exception error) {
                                            throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                        }
                                    }

                    (object[])

                        ✝︎ (마킹 값 치환)

                            {{__스네이크케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명_단수__}}
                            {{__파스칼케이스_단수_띄어쓰기__}}

                        ✝︎ (매핑 코드)

                            definedPythonFile
                                .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                    for (Map<String, Object> resourceConfig : resourceConfigs) {
                                        try {
                                            @SuppressWarnings("unchecked")
                                            List<Map<String, Object>> {{__카멜케이스_파라미터명__}} = (List<Map<String, Object>>) resourceConfig.get("{{__스네이크케이스_파라미터명__}}");
                                            if ({{__카멜케이스_파라미터명__}} != null && !{{__카멜케이스_파라미터명__}}.isEmpty()) {
                                                List<String> {{__카멜케이스_파라미터명_단수__}}Strings = new ArrayList<>();
                                                for (Map<String, Object> item : {{__카멜케이스_파라미터명__}}) {
                                                    List<String> keyValuePairs = new ArrayList<>();
                                                    for (Map.Entry<String, Object> entry : item.entrySet()) {
                                                        keyValuePairs.add(entry.getKey() + "{{__:__}}" + entry.getValue());
                                                    }
                                                    {{__카멜케이스_파라미터명_단수__}}Strings.add(String.join("{{__,__}}", keyValuePairs));
                                                }
                                                definedPythonFile
                                                    .append("        - \"")
                                                    .append(String.join("{{__/__}}", {{__카멜케이스_파라미터명_단수__}}Strings))
                                                    .append("\"\n"); 
                                            } else {
                                                definedPythonFile
                                                    .append("        - \"\"\n");
                                                log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                                                log.warn("{{__파스칼케이스_단수_띄어쓰기__}} does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                                                log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                                            }
                                        } catch (Exception error) {
                                            throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                        }
                                    }

                    (<string[]>[])

                        ✝︎ (마킹 값 치환)

                            {{__스네이크케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명__}}
                            {{__카멜케이스_파라미터명_단수__}}
                            {{__파스칼케이스_단수_띄어쓰기__}}

                        ✝︎ (매핑 코드)

                            definedPythonFile
                                .append("    {{__스네이크케이스_파라미터명__}}:\n");
                                    for (Map<String, Object> resourceConfig : resourceConfigs) {
                                        try {
                                            @SuppressWarnings("unchecked")
                                            List<List<String>> {{__카멜케이스_파라미터명__}} = (List<List<String>>) resourceConfig.get("{{__스네이크케이스_파라미터명__}}");
                                            if ({{__카멜케이스_파라미터명__}} != null && !{{__카멜케이스_파라미터명__}}.isEmpty()) {
                                                List<String> {{__카멜케이스_파라미터명_단수__}}Strings = new ArrayList<>();
                                                for (List<String> item : {{__카멜케이스_파라미터명__}}) {
                                                    String joinedInner = String.join("{{__,__}}", item);
                                                    {{__카멜케이스_파라미터명_단수__}}Strings.add(joinedInner);
                                                }
                                                definedPythonFile
                                                    .append("        - \"")
                                                    .append(String.join("{{__/__}}", {{__카멜케이스_파라미터명_단수__}}Strings))
                                                    .append("\"\n"); 
                                            } else {
                                                definedPythonFile
                                                    .append("        - \"\"\n");
                                                log.warn("------------------------------------------------------------------------------------------------------------------------------------");
                                                log.warn("{{__파스칼케이스_단수_띄어쓰기__}} does not exist, so it was processed as ''. The location of the class is handler/tofu/module/implementations/TofuModule.java");
                                                log.warn("------------------------------------------------------------------------------------------------------------------------------------");  
                                            }
                                        } catch (Exception error) {
                                            throw new RuntimeException("build_tofu_module_config_file_{{__스네이크케이스_파라미터명__}}");
                                        }
                                    }

                ✨ tags 파라미터를 사용하는지 확인한다.
                ✨ yaml 리프 노드를 지정한다.

            ✝︎ (implementations package) ⇢ tofu_resource

                📁 handler
                    ⤷ 📁 tofu
                        ⤷ 📁 resource
                            ⤷ 📁 implementations
                                ⤷ 📄 TofuResource.class

                (backslash 치환)

                    📁 terraform_resource
                        ⤷ 📁 module_name
                            ⤷ 📄 main.tf

                ✨ main.tf 파일을 main.backslash.txt 파일로 복제하고, " 를 \" 로 치환한다.
                ✨ tags 파라미터를 사용하는지 확인한다.
                ✨ lifecycle ignore_changes 를 확인한다.

        » (service package)

            ✝︎ (implementations package)

                📁 service
                    ⤷ 📁 implementations
                        ⤷ 📄 CleanUpDirectory.class
                        ⤷ 📄 DeleteDraftVersion.class
                        ⤷ 📄 DuplicateDraftVersions.class
                        ⤷ 📄 EntitySaveResource.class
                        ⤷ 📄 ExecTofuApply.class
                        ⤷ 📄 ExecTofuDestroy.class
                        ⤷ 📄 ExecTofuPlan.class
                        ⤷ 📄 ExecTofuPlanDestroy.class
                        ⤷ 📄 ExistsDraftVersion.class
                        ⤷ 📄 ExistsResourceSaveName.class
                        ⤷ 📄 IssueUniqueId.class
                        ⤷ 📄 JsonNodeLoadResource.class
                        ⤷ 📄 LoadDraftVersion.class
                        ⤷ 📄 PythonLoadResource.class
                        ⤷ 📄 TofuModule.class
                        ⤷ 📄 TofuResource.class

                » (TofuModule class) 📌 textarea 타입의 경우 해당

                    (파일명 정규화)

                        ✝︎ (주석)

                            /** File extension varies by textarea parameter */

                    ✨ 조건 분기에 {{__카멜케이스_파라미터명__}} 을 지정한다.
                    ✨ 조건 분기에서 파일 확장자를 지정한다.

        » (servlet package)

            ✝︎ (filter package)

                📁 servlet
                    ⤷ 📁 filter
                        ⤷ 📄 CustomCsrfFilter.class
                        ⤷ 📄 DomainFilter.class
                        ⤷ 📄 JwtAccessTokenFilter.class
                
                (DomainFilter class)

                    ✨ ALLOWED_HOST 멤버 변수를 application.properties를 활용하여 수정한다.

        (util package)

            📁 util
                ⤷ 📄 AutoCloseUtils.class
                ⤷ 📄 EmitterUtils.class
                ⤷ 📄 ExceptionUtils.class
                ⤷ 📄 FunctionUtils.class
                ⤷ 📄 GeneralUtils.class
                ⤷ 📄 ProcessUtils.class
                ⤷ 📄 TransactionalUtils.class

            (TransactionalUtils class)

                ✨ get{{__파스칼케이스_파라미터명__}} 지정하여 LAZY 로딩을 강제 초기화한다.

(나) 
(1) 배포.

    📁 opentofu
        ⤷ 📁 opentofu-backend
            ⤷ 📁 module_name

    ✝︎ (커맨드)

        mvn clean install;
        sudo rsync -avz --no-owner --no-group --progress ./target/mysql-0.0.1-SNAPSHOT.jar sysadmin@java.opentofu.click:/home/sysadmin/deploy/target/;
        rm -rf ./target/mysql-0.0.1-SNAPSHOT.jar;
        
        ls -al ~/.m2/repository/click/opentofu/mysql/0.0.1-SNAPSHOT/mysql-0.0.1-SNAPSHOT.jar;
        rm -rf ~/.m2/repository/click/opentofu/mysql/0.0.1-SNAPSHOT/mysql-0.0.1-SNAPSHOT.jar;
        
        echo "";
        echo "";
        echo "";
        echo "=== jar 파일 검색 ===";
        find ~ -type f -name "*0.0.1-SNAPSHOT.jar" -exec ls -lh {} \; 2>/dev/null;

(다) 
(1) 프로세스 실행.

    📁 home
        ⤷ 📁 sysadmin
            ⤷ 📁 deploy
                ⤷ 📁 target
                ⤷ 📁 nohup-logs

    ✝︎ (커맨드)

        nohup java -jar ~/deploy/target/mysql-0.0.1-SNAPSHOT.jar > ~/deploy/nohup-logs/mysql-output.log 2>&1 &
        nohup java -Djavax.net.ssl.trustStore=/opt/app/certs/truststore.p12 -Djavax.net.ssl.trustStorePassword=changeit -jar ~/deploy/target/mysql-0.0.1-SNAPSHOT.jar > ~/deploy/nohup-logs/mysql-output.log 2>&1 &

        ✨ EC2 에서는 데몬으로 실행한다.

(2) 프로세스 포트 조회.

    ✝︎ (커맨드)
    
        lsof -i :{{__JAVA_포트__}}

        tail -f -n 100 ~/deploy/nohup-logs/mysql-output.log

(3) 테이블 조회.

    ✝︎ (MySQL Workbench)

        ✨ sprout_entify 확인.

(4) sso public key 조회. 📌 서버 마이그레이션의 경우 해당

    ✝︎ (로컬 정적 호스트 매핑)

        📁 etc
            ⤷ 📄 hosts

        » (vm-java)

            (커맨드)

                sudo vi /etc/hosts
            
            (호스트 매핑)

                192.168.35.95       java-sso.opentofu.click








=end
