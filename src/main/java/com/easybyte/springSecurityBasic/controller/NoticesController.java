package com.easybyte.springSecurityBasic.controller;

import com.easybyte.springSecurityBasic.model.Notice;
import com.easybyte.springSecurityBasic.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class NoticesController {

  private final NoticeRepository noticeRepository;

  @GetMapping("/notices")
  public ResponseEntity<List<Notice>> getNotices() {
    List<Notice> notices = noticeRepository.findAllActiveNotices();
    if (notices != null) {
      /*
        .cacheControl(CacheControl.maxAge(Duration.ofSeconds(60)))
            요청 시 캐시메모리에 60초 동안 요청한 데이터를 보관한다.
            60초동안 재요청시 백앤드 요청(해당 api 호출)은 없다.
       */
      return ResponseEntity.ok()
              .cacheControl(CacheControl.maxAge(Duration.ofSeconds(60)))
              .body(notices);
    } else {
      return null;
    }
  }

}
