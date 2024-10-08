import { useNavigate } from "react-router-dom";

type Creator = {
  creatorId: number;
  creatorName: string;
  profileImg: string;
};

interface CreatorProps {
  creator: Creator;
}

function CreatorInfo({ creator }: CreatorProps) {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col hover:cursor-pointer items-center transform transition-transform duration-300 hover:translate-y-1">
      <img
        src={creator.profileImg}
        alt="프로필이미지"
        className="w-[150px] h-[150px] bg-gray-300 rounded-full mb-2.5"
        onClick={() => navigate(`/creator/${creator.creatorId}`)}
        role="presentation"
      />
      <div>{creator.creatorName}</div>
    </div>
  );
}

export default CreatorInfo;
