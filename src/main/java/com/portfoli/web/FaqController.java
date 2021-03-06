package com.portfoli.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.portfoli.domain.Faq;
import com.portfoli.domain.Pagination;
import com.portfoli.domain.QnaCategory;
import com.portfoli.service.BoardAttachmentService;
import com.portfoli.service.BoardService;
import com.portfoli.service.FaqService;
import com.portfoli.service.MemberService;
import com.portfoli.service.QnaCategoryService;


@Controller
@RequestMapping("faq")
public class FaqController {
  final int pageSize = 10;
  static Logger logger = LogManager.getLogger(FaqController.class);

  public FaqController() {
    FaqController.logger.debug("QnaController 객체 생성!");
  }

  @Autowired
  ServletContext servletContext;

  @Autowired
  BoardAttachmentService boardAttachmentService;

  @Autowired
  BoardService boardService;

  @Autowired
  FaqService faqService;

  @Autowired
  MemberService memberService;

  @Autowired
  QnaCategoryService qnaCategoryService;

  // @Autowired
  // private UserMailSendService mailsender;

  @GetMapping("list")
  public void list(@ModelAttribute("faq") Faq faq,
      Model model, @RequestParam(defaultValue="1") int curPage) throws Exception {

    // 전체리스트 개수
    int listCnt = faqService.selectListCntForUser(faq);

    Pagination pagination = new Pagination(listCnt, curPage);
    pagination.setPageSize(pageSize);// 한페이지에 노출할 게시글 수

    faq.setStartIndex(pagination.getStartIndex());
    faq.setPageSize(pagination.getPageSize());

    // 전체리스트 출력
    model.addAttribute("listCnt", listCnt);
    model.addAttribute("pagination", pagination);
    
    List<Faq> faqs = faqService.listForUser(pagination.getCurPage(), pagination.getPageSize());
    
    model.addAttribute("list", faqs);
  }

  @GetMapping("detail")
  public ModelAndView detail(HttpServletRequest request, HttpServletResponse response,
      HttpSession session, int number) throws Exception {
    
    Map<String, Object> params = new HashMap<>();
    ModelAndView mv = new ModelAndView();
    Faq faq = faqService.get(number);

    // 새로고침 조회수 막기
    Cookie[] cookies = request.getCookies();
    Cookie viewCookie = null;

    if (cookies != null && cookies.length > 0) {
      for (int i = 0; i < cookies.length; i++) {
        if (cookies[i].getName().contentEquals("cookie" + number)) {
          viewCookie = cookies[i];
        }
      }
    }

    if (faq != null) {
      mv.addObject("faq", faq);

      if (viewCookie == null) {
        Cookie newCookie = new Cookie("cookie" + number, "|" + number + "|");
        response.addCookie(newCookie);

        int viewCount = faq.getViewCount() + 1;
        params.put("viewCount", viewCount);
        params.put("boardNo", number);
        boardService.addViewCount(params);
      }
    }
    return mv;
  }

  @RequestMapping("delete")
  public String delete(int number) throws Exception {
    faqService.delete(number);
    return "redirect:/app/faq/list";
  }

  @GetMapping("updateForm")
  public void updateForm(Model model, int number) throws Exception {
    Faq faq = faqService.get(number);
    List<QnaCategory> categories = qnaCategoryService.get();
    model.addAttribute("faq", faq);
    model.addAttribute("categories", categories);
  }

  @RequestMapping("update")
  public String update(Faq faq) throws Exception {
    int number = faq.getNumber();
    faqService.update(faq);
    // return "redirect:/app/faq/detail?number=" + number;
    return "redirect:/app/faq/list";
  }

  /*
   */
  @GetMapping("form")
  public void addForm(Model model) throws Exception {
    List<QnaCategory> categories = qnaCategoryService.get();
    model.addAttribute("categories", categories);
  }

  @PostMapping("add")
  public String add(Faq faq, HttpServletRequest request) throws Exception {
    System.out.println("faq입니다아");
    System.out.println(faq);
    faqService.add(faq);
    return "redirect:/app/faq/list";
  }

}
