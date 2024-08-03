function Payment() {
  return (
    <div className="w-[1200px] h-[675px] bg-primary-300 flex p-[50px]">
      <div className="bg-white w-[700px] rounded-2xl p-[50px]">
        <div>
          <h5 className="text-h5 mb-[10px]">예매자 확인</h5>
          <div className="border-2 border-solid border-gray-700 rounded-xl p-[10px] text-medium">
            <div className="flex">
              <span className="flex w-[70px]">이름</span>
              <span>서지흔</span>
            </div>
            <div className="flex">
              <span className="flex w-[70px]">이메일</span>
              <span>test@gmail.com</span>
            </div>
          </div>
        </div>
        <div>
          <h5 className="text-h5 mt-[20px] mb-[10px]">취소 수수료</h5>
          <table>
            <tr className="bg-gray-100 text-medium border-2 border-solid border-[#B9B9B9]">
              <th className="w-[200px] border-2 border-solid border-[#B9B9B9] py-1">
                취소일
              </th>
              <th className="w-[200px] py-1">취소수수료</th>
            </tr>
            <tr>
              <td className="border-2 border-solid border-[#B9B9B9] pl-[10px] py-[5px]">
                관림일 9일전 ~ 7일전까지
              </td>
              <td className="border-2 border-solid border-[#B9B9B9] pl-[10px] py-[5px]">
                티켓금액의 10%
              </td>
            </tr>
            <tr>
              <td className="border-2 border-solid border-[#B9B9B9] pl-[10px] py-[5px]">
                관람일 6일전 ~ 3일전까지
              </td>
              <td className="border-2 border-solid border-[#B9B9B9] pl-[10px] py-[5px]">
                티켓금액의 20%
              </td>
            </tr>
            <tr>
              <td className="border-2 border-solid border-[#B9B9B9] pl-[10px] py-[5px]">
                관람일 2일전
              </td>
              <td className="border-2 border-solid border-[#B9B9B9] pl-[10px] py-[5px]">
                티켓금액의 30%
              </td>
            </tr>
          </table>
          <ul className="list-disc ml-5 mt-3">
            <li className="text-red">취소기한 : 2024년 7월 14일(일) 17:00</li>
            <li className="text-red">
              예매수수료는 예매일 이후 취소시에는 환불되지 않는다.
            </li>
            <li>
              단, 예매 당일 밤 12시 이전 취소시에는 취소수수료 없음 (취소기한
              내에 한함)
            </li>
          </ul>
        </div>
        <div className="mt-7 flex items-center">
          <input
            type="checkbox"
            name="agree"
            id="agree"
            className="w-[17px] h-[17px] mr-2 "
          />
          <label htmlFor="agree" className="text-medium">
            (필수) 취소수수료/취소기한을 확인하였으며, 동의합니다.
          </label>
        </div>
      </div>
      <div className="px-[40px] py-[50px] flex flex-col items-center">
        <div className="flex flex-col justify-start">
          <h4 className="text-h4 mb-[20px]">결제정보</h4>
          <div className="mb-[5px] flex">
            <span className="flex w-[100px]">팬미팅</span>
            <span>어쩌면 팬미팅</span>
          </div>
          <div className="mb-[5px] flex">
            <span className="w-[100px] flex">일시</span>
            <span>2024년 7월 15일 (월) 18:00</span>
          </div>
          <div className="mb-[5px] flex">
            <span className="w-[100px] flex">취소기한</span>
            <span className="text-red">2024년 7월 14일 (일) 17:00</span>
          </div>
          <div className="mb-[5px] flex">
            <span className="w-[100px] flex">취소 수수료</span>
            <div className="flex flex-col">
              <span className="text-red">티켓금액의 0 ~ 30%</span>
              <span className="text-gray-500">
                (상세 정보는 &apos;예매 안내&lsquo; 탭 참고)
              </span>
            </div>
          </div>
        </div>
        <div className="w-[250px] border-2 border-solid border-gray-600 p-[20px] flex justify-between my-[70px]">
          <span>총 결제 포인트</span>
          <span className="text-primary">5,000</span>
        </div>
        <button type="button" className="btn-lg px-[50px]">
          결제하기
        </button>
      </div>
    </div>
  );
}

export default Payment;
