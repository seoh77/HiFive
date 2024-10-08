import Carousel from "./MainPage.Carousel";

interface TicketListProps {
  nickname: string | null;
}

const TicketList: React.FC<TicketListProps> = ({ nickname }) => (
  <div className="w-full max-w-[1200px] flex flex-col h-[660px] bg-gray-600 shadow-inner-shadow rounded-3xl py-5  items-center overflow-hidden mx-auto mb-10">
    <span className="text-h5 text-white font-semibold mb-5">
      {nickname}님이 예매한 티켓
    </span>
    <Carousel />
  </div>
);

export default TicketList;
