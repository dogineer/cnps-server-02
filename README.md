<h1>📺 Media-Center</h1>

---

## 1. 개요
미디어센터는 주로 컨버팅과 트랜스코딩 서비스를 제공하는 플랫폼입니다.

사용자가 NPS 브라우저를 통해 인제스트를 요청하면, 미디어센터는 'FFmpeg'이라는 도구를 활용하여 다양한 형식과 포맷의 미디어 파일을 변환하고 가공합니다.<br>

컨버팅은 동일한 형식의 파일을 다른 버전으로 변환하는 작업을 의미하며, 주로 파일 크기나 호환성을 개선하는 데 활용됩니다. 
반면에 트랜스코딩은 원본 파일을 다른 형식으로 변환하는 작업으로, 다른 장치나 플랫폼에서 재생을 위해 사용되며, 파일 크기 조절이나 플랫폼 요구 사항 충족에 유용합니다.

따라서, 미디어센터는 컨버팅과 트랜스코딩을 통해 파일 형식, 코덱, 해상도, 비트율 등을 조정하여 비디오나 오디오 파일을 최적화하고, NLE 편집에 적합한 형태로 가공하는 역할을 수행합니다.

## 2. 서비스 구성
1. **영상 처리** : 
- NPS 브라우저에서 인제스트 요청시 정보를 받아와서 이에 대한 컨버팅과 트랜스코딩을 진행합니다. 
2. **메타데이터 가공** :
- 영상 처리가 끝난 후 메타데이터를 가공하여 MAM 데이터를 보냅니다. ( 자산 관리를 위해. )
3. **실시간 진행 상황**
- 컨버팅 또는 트랜스코딩의 진행 퍼센트를 계산 후 NPS 브라우저로 실시간 통신이 이루어집니다.
4. **메타데이터 추출** : ...

## 3. 프로젝트 기술 스택
java 11, spring, ffmpeg, restApi, ...

## 4. 어플리케이션 상세 기술
다음은 프로젝트의 어플리케이션 상세 기술입니다:
1. RestApi: RESTful 아키텍처를 기반으로 한 API를 개발하여 서버와의 통신을 처리합니다.
2. MSA (Microservices Architecture): MSA 아키텍처 패턴을 적용하여 각 기능을 독립적으로 개발하고 배포할 수 있도록 설계하였습니다.
3. 글로벌 예외 처리: 예외 상황을 전역적으로 처리하기 위한 글로벌 예외 처리 기능을 구현하였습니다.
4. 트랜스코딩 : ffmpeg를 사용하여 다양한 형식과 포맷의 미디어 파일을 변환합니다. ( 프리미어 프로의 최적의 편집을 위해 사용됩니다. )
5. 실시간 통신: 웹 소켓을 활용하여 실시간으로 트랜스코딩 작업의 진행 상황을 확인할 수 있는 기능을 구현하였습니다.
6. ...





