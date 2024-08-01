import { Route, Routes } from "react-router-dom";
import { useEffect } from "react";
import useAuthStore from "./store/useAuthStore";

import Navbar from "./components/Navbar/Navbar";
import ProfilePage from "./pages/ProfilePage/ProfilePage";
import Footer from "./components/Footer";
import BoardPage from "./pages/BoardPage/BoardPage";
import Mypage from "./pages/Mypage/MyPage";
import TicketList from "./pages/TicketPage/TicketPage.List";
import TicketDetail from "./pages/TicketPage/TicketPage.Detail";
import Question from "./pages/TicketPage/TicketPage.Question";
import StoryForm from "./pages/TicketPage/TicketPage.StoryForm";
import CreatorList from "./pages/CreatorListPage/CreatorListPage";

function App() {
  const fetchTokens = useAuthStore((state) => state.fetchTokens);
  const setAccessToken = useAuthStore((state) => state.setAccessToken);

  useEffect(() => {
    const localToken = localStorage.getItem("accessToken");

    if (!localToken) {
      fetchTokens();
    } else {
      setAccessToken(localToken);
    }
  }, [fetchTokens, setAccessToken]);

  return (
    <div className="App">
      <Navbar />
      <main>
        <Routes>
          <Route path="/creator/:creatorId" element={<ProfilePage />} />
          <Route path="/creator/:creatorId/:postId" element={<BoardPage />} />
          <Route path="/mypage/*" element={<Mypage />} />
          <Route path="/ticket" element={<TicketList />} />
          <Route path="/ticket/:fanmeetingId" element={<TicketDetail />} />
          <Route
            path="/fanmeeting/:fanmeetingId/question"
            element={<Question />}
          />
          <Route
            path="/fanmeeting/:fanmeetingId/story"
            element={<StoryForm />}
          />
          <Route path="/creator/list" element={<CreatorList />} />
        </Routes>
      </main>
      <Footer />
    </div>
  );
}

export default App;
